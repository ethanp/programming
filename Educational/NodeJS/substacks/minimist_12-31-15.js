/**
 * This "module" is really just a "function" that takes an
 * [array] of arguments, and an
 * @optional {object} of options
 *
 * This is a copy/paste, NOT a fork of the original repo, which is located at
 *
 *      https://github.com/substack/minimist
 *
 * Importing this module returns an object where boolean options are mapped to their boolean value,
 * and key-value options are keys mapped to their respective values,
 * and additional arguments are available as an array attached to the "_" key.
 *
 * To make this into a command line application, we should use it as follows
 *
 *      var argv = require('minimist')(process.argv.slice(2));
 *
 * This is because say we call the command line program as follows
 *
 *      node example/parse.js -x 3 -y 4 -n5 -abc --beep=boop foo bar baz
 *
 * We don't want to capture `node` or the program name as part of our argv, so we sliced it off.
 *
 * Now we will obtain from 'minimist' the following object
 *
 *      { _: [ 'foo', 'bar', 'baz' ],
 *        x: 3,
 *        y: 4,
 *        n: 5,
 *        a: true,
 *        b: true,
 *        c: true,
 *        beep: 'boop' }
 */
minimist = function (args, opts) {

    /* allow for no options to be passed in */
    if (!opts) opts = {};

    /* we'll use this to track which options were enabled */
    var flags = { bools: {}, strings: {}, unknownFn: null };

    /* I don't understand what the point of this is */
    if (typeof opts['unknown'] === 'function') {
        flags.unknownFn = opts['unknown'];
    }

    /* we can declare _all_ args to _always_ be treated as booleans */
    if (typeof opts['boolean'] === 'boolean' && opts['boolean']) {
        flags.allBools = true;
    } else {
        /* OR we can declare _particular_ args to _always_ be treated as booleans */
        // .filter(Boolean) means remove all "falsy" elements of the array.
        // In Javascript, falsy things include false, null, undefined, 0, NaN, and ''
        [].concat(opts['boolean']).filter(Boolean).forEach(function (key) {
            flags.bools[key] = true;
        });
    }

    /* with an alias, we can say that setting option A on the command line
       means that option B will be set */
    var aliases = {};

    // Object.keys(obj) gives us an array containing the keys in the `obj` argument
    Object.keys(opts.alias || {}).forEach(function (key) {

        /* store the aliases for this key passed in the `opts` object */
        // I think [].concat(...) is used to ensure we end up with an array value
        // in case the user messed-up in following instructions, without nasty
        // type-checking javascript code.
        aliases[key] = [].concat(opts.alias[key]);

        /* also store reverse mappings; basically, if the user passed `a: [b,c]`,
         * I think we should end up with {a: [b,c], b: [a,c], c: [a,b]}  */
        aliases[key].forEach(function (x) {
            aliases[x] = [key].concat(aliases[key].filter(function (y) {
                return x !== y;
            }));
        });
    });

    /* we can declare particular options to _always_ be treated as Strings */
    [].concat(opts.string).filter(Boolean).forEach(function (key) {
        flags.strings[key] = true;
        if (aliases[key]) {

            // I can't help but think this is a bug, see example code below
            /*
            > obj1 = {}
            > obj2 = {'a' : ['b', 'c']}
            > obj1[obj2['a']] = true
            > obj1
             => { 'b,c': true }
            */
            flags.strings[aliases[key]] = true;
        }
    });

    /* argument names may be given default values in-case they are not
     * specified on the command line */
    var defaults = opts['default'] || {};

    /* (Finally) initialize the object that will end up being returned by this function */
    var argv = { _: [] };

    /* set the default value for all the boolean options to false or their given defaults */
    Object.keys(flags.bools).forEach(function (key) {
        setArg(key, defaults[key] === undefined ? false : defaults[key]);
    });

    /* collect the arguments given after the double-dash; they'll eventually end up in '_' */
    var notFlags = [];
    if (args.indexOf('--') !== -1) {
        notFlags = args.slice(args.indexOf('--') + 1);
        args     = args.slice(0, args.indexOf('--'));
    }

    /* only used with the `unknownFn` crap */
    function argDefined(key, arg) {
        return (flags.allBools && /^--[^=]+$/.test(arg)) ||
                flags.strings[key] || flags.bools[key] || aliases[key];
    }

    /** add this value to this key's values */
    function setArg(key, val, arg) {

        /* I don't know what the point of this is */
        if (arg && flags.unknownFn && !argDefined(key, arg)) {
            if (flags.unknownFn(arg) === false) return;
        }

        /* if the value hasn't been declared to be a string, and it looks like a number,
         * cast it to type 'number'; otherwise, leave it as is  */
        var value = !flags.strings[key] && isNumber(val)
                    ? Number(val) : val;

        /* map this key to this value in the argv to return, appending if
         * there is (are) value(s) already there */
        setKey(argv, key.split('.'), value);

        /* do the same for any aliases of this key */
        (aliases[key] || []).forEach(function (x) {
            setKey(argv, x.split('.'), value);
        });
    }

    /** I can't find any succinct meaning in this function */
    function setKey(obj, keys, value) {

        // this copies `obj` by **reference** (i.e. NOT by value)
        var o = obj;

        // .slice(0, -1) will retain all but the last item in the array
        /* This function is always called with a command line arg split at '.'.
         * So this implements the semantics of an arg with '.'s in it, and I'm not sure
         * what the desired semantics of that are because I've never seen that before,
         * and it doesn't seem to be documented in the readme.
         */
        keys.slice(0, -1).forEach(function (key) {
            if (o[key] === undefined) o[key] = {};
            o = o[key];
        });

        /* if there's no '.' in the arg, this is the code that will always be executed */
        var key = keys[keys.length - 1];
        /* if this key doesn't have a value yet, or is supposed to be treated as a bool,
         * just assign the given value to it. */
        if (o[key] === undefined || flags.bools[key] || typeof o[key] === 'boolean') {
            o[key] = value;
        }
        /* if this key is mapped to an array, append the given value to it */
        else if (Array.isArray(o[key])) {
            o[key].push(value);
        }
        /* otherwise, create an array containing the current value, and then the new value */
        else {
            o[key] = [o[key], value];
        }
    }

    /**
     * @return boolean true iff any of this key's aliases
     *         have been declared to be boolean-only
     */
    function aliasIsBoolean(key) {
        // Array.prototype.some(callback) returns true if
        // the callback returns true for any array element
        return aliases[key].some(function (x) {
            return flags.bools[x];
        });
    }

    /* -- ======== Now we're talking  ======== -- */

    for (var i = 0; i < args.length; i++) {
        var arg = args[i];

        /* if it's a double dash with a value */
        if (/^--.+=/.test(arg)) {

            // substack says:
            // """Using [\s\S] instead of . because js doesn't support the
            //    'dotall' regex modifier. See:
            //    http://stackoverflow.com/a/1068308/13216"""
            /* get the key and the value */
            var m     = arg.match(/^--([^=]+)=([\s\S]*)$/);
            var key   = m[1];
            var value = m[2];

            /* handle bool-option appropriately */
            if (flags.bools[key]) {
                value = value !== 'false';
            }
            /* set the key to the value */
            setArg(key, value, arg);
        }

        /* e.g. --no-cake ==leads to==> cake: false */
        else if (/^--no-.+/.test(arg)) {
            var key = arg.match(/^--no-(.+)/)[1];
            setArg(key, false, arg);
        }

        /* double-dash with no value */
        else if (/^--.+/.test(arg)) {
            var key  = arg.match(/^--(.+)/)[1];

            /* maybe the next arg is the value? */
            var next = args[i + 1];

            /* if [all of]
             * 1. value exists
             * 2. isn't a command-line arg
             * 3. isn't declared a bool in the `opts`
             * 4. isn't an alias for a boolean
             *
             * then use the next arg as the value, and skip forward
             */
            if (next !== undefined && !/^-/.test(next)
                    && !flags.bools[key]
                    && !flags.allBools
                    && (aliases[key] ? !aliasIsBoolean(key) : true)) {
                setArg(key, next, arg);
                i++;
            }

            /* if the next is `true` or `false`, use it as value */
            else if (/^(true|false)$/.test(next)) {
                setArg(key, next === 'true', arg);
                i++;
            }

            /* if it's meant to be a string, it's the empty string */
            /* otherwise, it's just a bool "true" */
            else {
                setArg(key, flags.strings[key] ? '' : true, arg);
            }
        }

        /* if it's a single dash, then (any number of) letters */
        else if (/^-[^-]+/.test(arg)) {

            /* get all but the last (also shave off the dash in front) */
            var letters = arg.slice(1, -1).split('');

            var broken = false;
            for (var j = 0; j < letters.length; j++) {

                /* start by calling the SECOND letter `next` */
                var next = arg.slice(j + 2);

                /* I'm not sure what the goal here is...
                 * Its effect is to set the value of this letter (as key) to '-' if that
                 * was passed in the letter-string. E.g. -a-afe => {'a':'-'} ??
                 * It's just that I've never seen the command-line args used in that fashion. */
                if (next === '-') {
                    setArg(letters[j], next, arg);
                    continue; /* move on to the next letter in this string */
                }

                /* if this is of form -asdf=val ==produces==> {'a':'val'}
                 * [I verified this to be the case]
                 */
                if (/[A-Za-z]/.test(letters[j]) && /=/.test(next)) {
                    setArg(letters[j], next.split('=')[1], arg);
                    broken = true; /* skip the next chunk of code after breaking this for-loop */
                    break;
                }

                /* -a-234.432e-33 ==produces==> {'a':-234.432e-33} */
                if (/[A-Za-z]/.test(letters[j])
                        && /-?\d+(\.\d*)?(e-?\d+)?$/.test(next)) {
                    setArg(letters[j], next, arg);
                    broken = true;
                    break;
                }

                /* -asdf ==produces==> {'a':'df'} ?? that can't be right.... */
                // \W is "NOT word-like-character" equivalent to [^A-Za-z0-9_] (--MDN)
                if (letters[j + 1] && letters[j + 1].match(/\W/)) {
                    setArg(letters[j], arg.slice(j + 2), arg);
                    broken = true;
                    break;
                }

                /* if it's a string-only flag, it gets '',
                 * otw it is `true` (i.e. -asdf ==produces==> {'a':true} and repeat at j++)
                 */
                else {
                    setArg(letters[j], flags.strings[letters[j]] ? '' : true, arg);
                }
            }

            // get the last character of this arg
            var key = arg.slice(-1)[0];

            /* we made it to the end of the loop through chars 'naturally' */
            if (!broken && key !== '-') {
                /* if the next arg is not a dasher and not a bool, it is the value for this key */
                if (args[i + 1] && !/^(-|--)[^-]/.test(args[i + 1])
                        && !flags.bools[key]
                        && (aliases[key] ? !aliasIsBoolean(key) : true)) {
                    setArg(key, args[i + 1], arg);
                    i++;
                }
                /* this is code duplicated from above... */
                else if (args[i + 1] && /true|false/.test(args[i + 1])) {
                    setArg(key, args[i + 1] === 'true', arg);
                    i++;
                }
                else {
                    setArg(key, flags.strings[key] ? '' : true, arg);
                }
            }
        }
        else {
            /* it wasn't a dasher, so put in the '_' bag */
            if (!flags.unknownFn || flags.unknownFn(arg) !== false) {
                argv._.push(
                        flags.strings['_'] || !isNumber(arg) ? arg : Number(arg)
                );
            }

            /* put the rest of the args in the '_' list */
            if (opts.stopEarly) {
                argv._.push.apply(argv._, args.slice(i + 1));
                break;
            }
        }
    }

    /* use the specified defaults if no value has been assigned to those keys yet */
    Object.keys(defaults).forEach(function (key) {
        if (!hasKey(argv, key.split('.'))) {
            setKey(argv, key.split('.'), defaults[key]);

            (aliases[key] || []).forEach(function (x) {
                setKey(argv, x.split('.'), defaults[key]);
            });
        }
    });

    /* if specified, add the args after the '--' flag as an array on the ['--'] key */
    if (opts['--']) {
        argv['--'] = new Array();
        notFlags.forEach(function (key) {
            argv['--'].push(key);
        });
    }

    /* otw just add them to the ['-'] key */
    else {
        notFlags.forEach(function (key) {
            argv._.push(key);
        });
    }

    return argv;
};

/** In essence, just check if key in obj... */
function hasKey(obj, keys) {

    var o = obj;

    // we can ignore this, unless understanding the '.' crap
    keys.slice(0, -1).forEach(function (key) {
        o = (o[key] || {});
    });

    var key = keys[keys.length - 1];
    return key in o;
}

/**
 * @return boolean "true" iff either
 *  1. x is of type 'number'
 *  2. x is a string in the common hexadecimal format
 *  3. x is a string that looks like a number, e.g. the following
 *      a. '+234.432e-234'
 *      b. '-.234'
 *      c. '23'
 */
function isNumber(x) {
    if (typeof x === 'number') return true;
    if (/^0x[0-9a-f]+$/i.test(x)) return true;
    // (?:asdf) is a "non-capturing group"; it creates a group, but doesn't capture its contents
    return /^[-+]?(?:\d+(?:\.\d*)?|\.\d+)(e[-+]?\d+)?$/.test(x);
}

console.log(minimist(['-asdf=asdf']));
