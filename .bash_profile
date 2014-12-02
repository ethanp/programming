# TODO
# Cron jobs for
#   brew update && brew upgrade


######################
#  Terminal Aliases  #
######################

# Aliases: if you use "quotes", then the substitution is made here in the _profile
#           but if you use 'aposts', then the substitution is done at call-time
#   You probably want 'single quotes' if you're using variables
#       Or you may just want to use a function instead as I did with cs(){}
alias h='cd ~'
alias gohome='cd ~'
alias ls='ls -AFG'
alias rm='rm -i'   # a safety precaution
alias grep='grep --color'
alias egrep='egrep --color'
alias ll='ls -lh'  # longform, human-readable sizes (as opposed to machine-readable)
alias lg='ls | grep'
alias llg='ls -lh | grep'
alias hisg='history | grep' # (hg is taken by mercurial)
alias bug='brew update && brew upgrade'
alias vimrc='sb ~/.vimrc'
alias bprof='sb ~/.bash_profile'
alias this='export PATH="${PATH}:."'
alias sb='/Applications/Sublime\ Text\ 2.app/Contents/SharedSupport/bin/subl -n $@'
alias sba='/Applications/Sublime\ Text\ 2.app/Contents/SharedSupport/bin/subl -a $@'
alias ut='ssh -o ServerAliveInterval=10 ethanp@almond-joy.cs.utexas.edu'
alias utx='ssh -o ServerAliveInterval=10 -X ethanp@almond-joy.cs.utexas.edu'
alias tracker='cs ~/Dropbox/CSyStuff/TTRails/tracker'
alias trracker='cs ~/Dropbox/CSyStuff/TTRails/trracker'
alias play_activator='~/Applications/activator-1.0.10/activator ui'
alias blog='cs ~/code/personal_project_use/libraries_to_use/Ruby/octopress/'
alias octop='cs /Users/Ethan/code/personal_project_use/libraries_to_use/Ruby/octopress'
if [[ -d ~/Dropbox/CSyStuff/ProgrammingGit ]]; then
    PROGRAMMINGGIT=~/Dropbox/CSyStuff/ProgrammingGit
else
    PROGRAMMINGGIT=~/code/programming
fi
alias ca='cs $PROGRAMMINGGIT/StuffIWrote/Scala/CommentAnalyzer/CommentAnalyzer_0'
alias playakka='cs $PROGRAMMINGGIT/StuffIWrote/Scala/akka-redis-websockets-play-scala_translation'
alias notes='cs $PROGRAMMINGGIT/Notes'

# Operating Systems class
alias empat='ssh ethan@empat.csres.utexas.edu'

######################
#  Terminal Options  #
######################

# 4lolz
set -o vi

# turn on extra metacharacters: (?|*|+|@|!)(pattern)
shopt -s extglob

# R: Let `less` use colors,
# F: Just `cat` if it's only one screen worth of info
# X: "Disables sending the termcap initialization strings to the terminal." (?)
export LESS=-RFX

########################
#  Terminal Functions  #
########################

# print the length of the input (input needn't be quoted and can have spaces)
# but if the input has \" in it, this will not work; in that case use python
# or something
function len { echo -n "$@" | wc -c; }

# render manpage as postscript in Preview
function pman { man -t $1 | open -fa /Applications/Preview.app ; }

# run the "Really Barebones Minco" iCal interface I made
function minco { python $PROGRAMMINGGIT/StuffIWrote/Python/rbm/rbm.py $@ ; }

# ssh -X into specified CS Server
function uto { ssh -o ServerAliveInterval=30 -X ethanp@$1.cs.utexas.edu ; }

# send file to ut
function uts { scp "$1" ethanp@almond-joy.cs.utexas.edu: ; }

# download movie to Movies dir
function dlmov { cd ~/Desktop/Movies/ && youtube-dl -t $1  && cd - ; }

# pretty-print a raw JSON input file
function json { cat $1 | python -mjson.tool | less; }

# change & show
function cs { cd "$1"; ls; }

# as in Does Directory Have... ( -i := Case Insensitive )
function ddh { ls "$1" | grep -i "$2"; }

# supposed to enable autocomplete for rbenv
if which rbenv > /dev/null; then
    eval "$(rbenv init -)"
fi

# 5/27/14
# open mmd as pdf
function mtx {
    TEX_NAME=$(basename "$1" | sed s'|.md|.tex|')
    PDF_NAME=$(basename "$1" | sed s'|.md|.pdf|')
    LATEX_DIR=~/Desktop/Latex
    TEX_LOC=$LATEX_DIR/"$TEX_NAME"
    multimarkdown -t latex "$1" > "$TEX_LOC"
    pdflatex --output-directory "$LATEX_DIR" "$TEX_LOC" > /dev/null
    open -a /Applications/Preview.app "$LATEX_DIR"/"$PDF_NAME"
}

# remove the first 49 lines of each *.[mh] file in this- & sub-directories
# assembled from the following:
# stackoverflow.com/questions/9954680/how-to-store-directory-files-listing-into-an-array/15416377#15416377
# stackoverflow.com/questions/316590/how-to-count-lines-of-code-including-sub-directories/316613#316613
# stackoverflow.com/questions/15691942/bash-print-array-elements-on-separate-lines
function decrust {
    echo "The following files will have their first 48 lines removed:"
    echo
    files=($(find . -name "*.[mh]"))  # create array of names matching *.[mh]
    printf -- '%s\n' "${files[@]}"    # kinda cool that printf exists in bash
    if [ "$1" == "doit" ]; then
        for item in ${files[*]}; do
            tail +49 $item > ${item}.copy # put all but first 49 lines in buffer
            mv ${item}.copy $item         # overwrite original file with buffer
        done                              # doesn't work without the buffer file

    # learned my lesson from dedir(), put the do_nothing option in `else`
    # because with bash you never know what sort of crap is going to get to
    # your else statement
    else
        echo -e "\ndecrust is currently in dry mode, use \n\n\t$ decrust doit\n\nto actually run it\n"
    fi
}

# delete directory
function dedir {
    if [[ $# != 1 ]]; then
        echo "You must supply a single directory to demolish"
    elif [ -d "$1" ]; then
        tree $1
        echo "Enter 1 to remove $1 from the face of the Earth: "
        read response
        if (("$response" == "1")); then
            rm -fR $1
            echo "Action Completed"
        else
            echo "Action Cancelled"
        fi
    else
        echo "$1 is not a directory."
    fi
}


# Compile C programs with useful gcc flags
# ----------------------------------------
# TODO this could probably be upgraded using stuff from "Learn C the Hard Way"
#
# I've removed -O2 & -ffast-math optimization because I figure there are going
# to be more situations where I don't want something optimized out than where
# I really care about the speed of program execution.
function compile {
    if [[ $# == 0 ]]; then
        echo "This is a shortcut for compiling simple .c files with a whole lot of warnings enabled"
        echo "Usage: compile <inName> (<outName>)?"
    elif [[ $# > 2 ]]; then
        echo "You'll have to implement a better compile function to get that to work"
        echo "May I suggest using a for loop, or matching *.c & *.h"
    elif [[ -f "$1" ]]; then
        # I took out -Wstrict-prototypes -Wmissing-prototypes
        if [[ $# == 1 ]]; then
            gcc -W -Wall -fno-common -Wcast-align -std=c99 -Wredundant-decls\
                -Wbad-function-cast -Wwrite-strings -Waggregate-return $1
            echo "Your output is in a.out"
        else
            gcc -W -Wall -fno-common -std=c99 -Wcast-align -Wredundant-decls\
                -Wbad-function-cast -Wwrite-strings -Waggregate-return $1 -o $2
        fi
    else
        echo "Usage: compile <inName> (<outName>)?"
        echo "In your case, <inName> wasn't a file."
    fi
}

# run xl script (opt: -y, --yesterday)
function xlj {
    THEPROJECT=~/Dropbox/CSyStuff/ProgrammingGit/StuffIWrote/Java/Minco_XL
    THEJAR=out/artifacts/Minco_XL_jar/Minco_XL.jar
    java -jar $THEPROJECT/$THEJAR "$*"
}


##########
#  PATH  #
##########

# PATH="/Library/Frameworks/Python.framework/Versions/2.7/bin:${PATH}"
# PATH="/Library/Frameworks/Python.framework/Versions/3.3/bin:${PATH}" # switch to new Python
PATH="/Users/Ethan/Applications/javacc-5.0/bin:${PATH}"
PATH="/Users/Ethan/Applications/apache-ant-1.8.4/bin:${PATH}"
PATH="$PATH":~/Dropbox/CSyStuff/Google_depot_tools_git/depot_tools  # don't remember this
PATH="/Applications/Anaconda/anaconda/bin:$PATH"  # allows for "conda" command
PATH="$PATH":/usr/local/share/scala-2.10.1/bin    # I hope I'm not using this (TODO verify & remove)
PATH=${PATH}:$HOME/gsutil                         # this was for getting patent info
PATH="/usr/local/lib/ruby:${PATH}"                # not sure here bc I have like 5 Rubys
PATH=$PATH:$HOME/.rvm/bin                         # allows for "rvm" command
PATH="/Applications/Postgres93.app/Contents/MacOS/bin:$PATH:$PATH" # for use with Postgres.app

# use homebrew's installations of bash and grep
# http://lapwinglabs.com/blog/hacker-guide-to-setting-up-your-mac
# I'm not clear that this is working though...
PATH=/usr/local/Cellar/coreutils/8.23/libexec/gnubin:$PATH

# accommodate directory-structure differences between two computers
if [[ -d ~/code/fuzzycd ]]; then
    FUZZYCD=~/code/fuzzycd
else  # TODO why doesn't this work when surrounded by either '' or "" ??
    FUZZYCD=~/code/personal_project_use/code_to_base_off_of/Ruby/fuzzycd
fi
PATH=$FUZZYCD:$PATH                 # add fuzzycd, may have to precede RVM in PATH
PATH="/usr/local/bin:${PATH}"       # Homebrew comes first
export PATH

# install https://github.com/rupa/z cd utility
. /Users/Ethan/code/personal_project_use/libraries_to_use/ShellUtil/z/z.sh

############
#  CDPATH  #
############

# These are the places the "cd" command will LOOK, in this order too
CDPATH="::"                         # Current Directory
CDPATH="${CDPATH}:$HOME"            # Global Var == /Users/Ethan
CDPATH="${CDPATH}:${HOME}/Dropbox"  # add Dropbox to the list (TODO doesn't work)
export CDPATH


################
#  PYTHONPATH  #
################

# PYTHONPATH is where Python looks for user-defined Modules/Packages
#  after searching the current directory
PYTHONPATH="/usr/local/lib/python2.7/site-packages:$PYTHONPATH"
# PYTHONPATH="/Applications/Anaconda/anaconda/lib/python2.7/site-packages:$PYTHONPATH"
export PYTHONPATH


###############
#  CLASSPATH  #
###############

# tells java where to look for classes referenced by your program
# e.g: import my.package.Foo
CLASSPATH=.:/usr/share/java/commons-math3-3.2  # this is legacy, now I just use Maven
export CLASSPATH


###############
#  JAVA_HOME  #
###############

#JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home
#export JAVA_HOME

# This is the reason I have JAVA_HOME set:
# Summary: running `pig` requires it

# brew install pig
# ==> Downloading http://www.apache.org/dyn/closer.cgi?path=pig/pig-0.12
###################################################
# ==> Caveats                                     #
# You may need to set JAVA_HOME:                  #
#    export JAVA_HOME="$(/usr/libexec/java_home)" #
###################################################
# ==> Summary
# 🍺  /usr/local/Cellar/pig/0.12.0: 8 files, 26M, built in 7.9 minutes
# ┌──[~/Desktop/Movies]--[ 4:49 PM ]
# └──╼pig
# Error: JAVA_HOME is not set.
# ┌──[~/Desktop/Movies]--[ 4:58 PM ]
# └──╼export JAVA_HOME="$(/usr/libexec/java_home)"
# ┌──[~/Desktop/Movies]--[ 5:00 PM ]
# └──╼pig
# grunt>
export JAVA_HOME="$(/usr/libexec/java_home)"

# These are only saved _this_ session
HISTSIZE=2000

# These are saved _between_ sessions in .bash_history
HISTFILESIZE=2000   # TODO it's unclear why this file is only ~850 lines/~2 months worth

# Load RVM into a shell session *as a function*
[[ -s "$HOME/.rvm/scripts/rvm" ]] && source "$HOME/.rvm/scripts/rvm"

# Brew tab-completion (works) [https://github.com/miku/brew-completion]
if [ -f ~/code/brew-completion/brew-completion.sh ]; then
    . ~/code/brew-completion/brew-completion.sh
fi

# Must overwrite cd-command after loading rvm bc rvm redefines cd too
source $FUZZYCD/fuzzycd_bash_wrapper.sh


#####################
# The Cool Terminal #
#####################

# from https://bbs.archlinux.org/viewtopic.php?pid=1068202#p1068202

# Define colornames
NC='\033[0m'   # No Color
yellow='\033[0;33m'
green='\033[0;32m'
bright_cyan='\033[0;96m'
white='\033[0;37m'
red='\033[0;31m'

PROMPT_ERROR="echo \"[\[${red}\]\342\234\227\[${white}\]]\342\224\200\")"

# the \NUMs are the composable pieces of the 'bar' in the prompt
PRE_PROMPT="\[${white}\]\342\224\214\342\224\200\$([[ \$? != 0 ]] && ${PROMPT_ERROR}"
PROMPT="\[${white}\]\342\224\200[\[${green}\]\${DIRSTACK}\[${white}\]]\$(promptFill)"
POST_PROMPT="\n\[${white}\]\342\224\224\342\224\200\342\224\200\342\225\274\[${NC}\]"

# PS1 := prompt-bar
PS1="${PRE_PROMPT}${PROMPT}${POST_PROMPT}"

function promptFill {
    NOW=$(date +"[%l:%M %p ]")
    if [[ $PREV_ERROR != 0 ]]; then  # add a little 'x' to prompt if the last thing bombed out
        local string="xx[x]--[${DIRSTACK}]"
    fi
    echo -ne "--"  # -n: no newline at the end; -e: enable interpretation of escape sequences
    echo -n $NOW
    return $PREV_ERROR
}

