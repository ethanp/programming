/**
 * Started 4/5/14 Ethan Petuchowski
 * The goal here is to unminify the implementation of the game 2048 from
 * https://gist.github.com/justecorruptio/9967738, as a challenge.  It seems
 * the most important thing to know is C operator precedence, and the different
 * uses of the <COMMA> operator, which I've never seen used like that.
 * My first draft stopped compiling after a lot of editing, so I need be careful.
 */

/**
 * Questions:
 * ==========
 *
 * What does `puts("\e[2J\e[H");` do?
 *     * puts: print string followed by newline
 *     * \e[2J: clears the screen by inserting enough newlines that the old content is off the screen
 *     * \e[H:  in this case, it changes the clearing of the screen by `e[2J` so that it only involves
 *              writing one newline and scrolling the screen itself so the old content is no longer
 *              visible
 *     * See escape-sequences.c in this dir
 *
 * How are nested ?:'s parsed?
 *     * I think it's like
 *         * This:        a ?  b ?  c ? d : e  : f  : g
 *         * equals This: a ? (b ? (c ? d : e) : f) : g
 *
 * What does that comma mean?
 *     * It evaluates each comma separated thing, but only returns the value and type of the last one
 *     * It also doesn't count as a full statement, so you can stuff a bunch of stuff in a for-body
 *         without closing-out the for-loop
 *     * See comma-operator.c in this dir
 *
 * What is the default size (e.g. M[16]), int? void *?
 *     =?> I believe it's `int`
 *
 * What does `system("stty cbreak");` do?
 *     => read just one char from stdin and don't require "return" key
 *
 * The "s" function is declared to take 6 parameters, but it's only called twice in
 * the code, and both times with 2 parameters. What's going on?
 */

// Implicit ints
M[16], // the game board
X=16,  // the game board size
W,     // whether you won or not
k;

main()
{
    // pass each keyboard keypress to T function
    T(system("stty cbreak"));

    puts(W & 1 ? "WIN" : "LOSE");
}

K[]={ 2, 3, 1 };    // possibly for interpretting the arrow keys

// The "s" function is declared to take 6 parameters, but it's only called twice in
// the code, and both times with 2 parameters. What's going on?
s(f,d,i,j,l,P) {
    for(i = 4; i--;) // by going backwards, i will be 0, and the loop will terminate
        for(j=k=l=0; k < 4;)
            j < 4 ? P = M[w(d,i,j++)],  // this comma is always executed,
                                        // but it's not a full statement,
                                        // so the line doesn't finish
                                        // and therefore we don't need brackets

            W |= P >> 11,   // ?? no idea
            l * P && (f ? M[w(d,i,k)] = l << (l == P) : 0, k++),
            l = l ? P ? l-P ? P : 0 : l : P : (f ? M[w(d,i,k)] = l : 0, ++k, W |= 2 * !l, l = 0);
}

// Locate the position of what is currently (i,j) but after the board
// has been rotated d times.
//
// `4*i + j` will find a 2D coordinate in the game board 1D array
// What do the intermediate `d` iterations do?
//      w(2, 1, 1) will go => w(1, 1, 2) => w(0, 2, 2)
//      w(1, 2, 3) will go => w(0, 3, 1)
//   OH! it's rotating the board `d` times. cool.
w(d,i,j) {
    return d ? w(d-1, j, 3-i) : 4*i + j;
}

T(i) {
    for(i = X + rand() % X; M[i%X] * i; i--); // this `i` hides the param, right?
    i ? M[i%X] = 2 << rand() % 2 : 0;         // this `i` is the param, right?
    for(W=i=0; i < 4;)
        s(0, i++);
    for(i = X, puts("\e[2J\e[H"); i--; i % 4 || puts(""))
        printf(M[i] ? "%4d|" : "    |", M[i]);          // either print the value or an empty square
    W-2 || read(0, &k, 3) | T( s(1, K[(k>>X) % 4]) );
}
