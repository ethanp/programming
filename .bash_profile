# TODO
# Cron jobs for
#   brew update && brew upgrade

# Aliases: if you use "quotes", then the substitution is made here in the _profile
#           but if you use 'aposts', then the substitution is done at call-time
#   You probably want 'single quotes' if you're using variables
#       Or you may just want to use a function instead as I did with cs(){}
alias h='cd ~'
alias ls='ls -AFG'
alias rm='rm -i'
alias grep='grep --color'
alias ll='ls -l'
alias lg='ls | grep'
alias llg='ls -l | grep'
alias hisg='history | grep'
alias bug='brew update && brew upgrade'
alias vimrc='mvim ~/.vimrc'
alias bprof='mvim ~/.bash_profile'
alias this='export PATH="${PATH}:."'
alias sb='/Applications/Sublime\ Text\ 2.app/Contents/SharedSupport/bin/subl -n $@'
alias ut='ssh -o ServerAliveInterval=30 ethanp@almond-joy.cs.utexas.edu'
alias utx='ssh -o ServerAliveInterval=10 -X ethanp@almond-joy.cs.utexas.edu'
alias tracker='cs /Users/Ethan/Dropbox/School\ Help\ Files/Tracker'
if [[ -d ~/Dropbox/CSyStuff/ProgrammingGit ]]; then
    PROGRAMMINGGIT=~/Dropbox/CSyStuff/ProgrammingGit
else
    PROGRAMMINGGIT=~/code/programming
fi


# 4lolz
set -o vi

# turn on extra metacharacters: (?|*|+|@|!)(pattern)
shopt -s extglob


function minco { python $PROGRAMMINGGIT/StuffIWrote/Python/rbm/rbm.py $@ ; }

# ssh -X into specified CS Server
function uto { ssh -o ServerAliveInterval=30 -X ethanp@$1.cs.utexas.edu ; }

# send file to ut
function uts { scp "$1" ethanp@almond-joy.cs.utexas.edu: ; }

# download movie to Movies dir
function dlmov { cd ~/Desktop/Movies/ && youtube-dl -t $1 && cd -; }

# pretty-print raw JSON
function json { cat $1 | python -mjson.tool | less; }

# change & show
function cs { cd "$1"; ls; }

# as in Does Directory Have... ( -i := Case Insensitive )
function ddh { ls "$1" | grep -i "$2"; }

# remove the first 49 lines of each *.[mh] file in this- & sub-directories
# assembled from the following:
# stackoverflow.com/questions/9954680/how-to-store-directory-files-listing-into-an-array/15416377#15416377
# stackoverflow.com/questions/316590/how-to-count-lines-of-code-including-sub-directories/316613#316613
function decrust {
    files=($(find . -name "*.[mh]"))  # create array of names matching *.[mh]
    for item in ${files[*]}; do
        tail +49 $item > ${item}.copy # put all but first 49 lines in buffer
        mv ${item}.copy $item         # overwrite original file with buffer
    done                              # doesn't work without the buffer file
}

# delete directory
function dedir {
    if [[ $# != 1 ]]; then
        echo "You must supply a single directory to demolish"
    elif [ -d "$1" ]; then
        tree $1
        echo "Enter 1 to remove $1 from the face of the Earth: "
        read response
        if [ $response -ne 1 ]; then
            echo "Action Cancelled"
        else
            rm -fR $1
            echo "Action Completed"
        fi
    else
        echo "$1 is not a directory."
    fi
}

# I've removed -O2 & -ffast-math optimization because I figure there are going
# to be more situations where I don't want something optimized out than where I
# really care about the speed of program execution.
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

# PATH="/Library/Frameworks/Python.framework/Versions/2.7/bin:${PATH}"
# PATH="/Library/Frameworks/Python.framework/Versions/3.3/bin:${PATH}" # switch to new Python
PATH="/Users/Ethan/Applications/javacc-5.0/bin:${PATH}"
PATH="/Users/Ethan/Applications/apache-ant-1.8.4/bin:${PATH}"
PATH="$PATH":~/Dropbox/CSyStuff/Google_depot_tools_git/depot_tools  # don't remember this
PATH="/Applications/Anaconda/anaconda/bin:$PATH"  # allows for "conda" command
PATH="$PATH":/usr/local/share/scala-2.10.1/bin
PATH=${PATH}:$HOME/gsutil                         # this was for getting patent info
PATH="/usr/local/lib/ruby:${PATH}"
PATH=$PATH:$HOME/.rvm/bin                         # allows for "rvm" command
if [[ -d ~/code/fuzzycd ]]; then
    FUZZYCD=~/code/fuzzycd
else  # TODO why doesn't this work when surrounded by either '' or "" ??
    FUZZYCD=~/code/personal_project_use/code_to_base_off_of/Ruby/fuzzycd
fi
PATH=$FUZZYCD:$PATH
PATH="/usr/local/bin:${PATH}"
export PATH

# These are the places the "cd" command will LOOK, in this order too
CDPATH="::"                         # Current Directory
CDPATH="${CDPATH}:$HOME"            # Global Var == /Users/Ethan
CDPATH="${CDPATH}:${HOME}/Dropbox"  # add Dropbox to the list
export CDPATH

# PYTHONPATH is where Python looks for user-defined Modules/Packages
#  after searching the current directory
PYTHONPATH="/usr/local/lib/python2.7/site-packages:$PYTHONPATH"
export PYTHONPATH

# tells java where to look for classes referenced by your program
# e.g: import my.package.Foo
CLASSPATH=.:/usr/share/java/commons-math3-3.2
export CLASSPATH

#JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home
#export JAVA_HOME

# 6/18/13
# from http://jeethurao.com/blog/?p=217 for getting Scala to work with Intellij
# (I don't think it made a difference, I'm still "using an external compiler")
# deleted this and see what happens: 1/15/14
# export JAVA_HOME=$(/usr/libexec/java_home)
# export SCALA_HOME=/usr/local/Cellar/scala/2.10.0/libexec
# export JAVACMD=drip
# export DRIP_SHUTDOWN=30
# export SBT_OPTS="-XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:PermSize=128M -XX:MaxPermSize=512M"

# These are only saved _this_ session
HISTSIZE=2000
# These are saved _between_ sessions in .bash_history
HISTFILESIZE=2000

# Load RVM into a shell session *as a function*
[[ -s "$HOME/.rvm/scripts/rvm" ]] && source "$HOME/.rvm/scripts/rvm"

# Brew tab-completion (works) [https://github.com/miku/brew-completion]
if [ -f ~/code/brew-completion/brew-completion.sh ]; then
    . ~/code/brew-completion/brew-completion.sh
fi

# Must overwrite cd-command after loading rvm bc rvm redefines cd too
source $FUZZYCD/fuzzycd_bash_wrapper.sh

##############################################################################
# The Cool Terminal from https://bbs.archlinux.org/viewtopic.php?pid=1068202#p1068202

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
### End Cool Terminal #########################################################

