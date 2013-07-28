# Aliases: if you use "quotes", then the substitution is made here in _profile
#           but if you use 'aposts', then the substitution is done at call-time
#   You probably want 'single quotes' if you're using variables
#       Or you may just want to use a function instead as I did with cs(){}
alias ls='ls -AFG'
alias rm='rm -i'
alias ll='ls -l'
alias lg='ls | grep'
alias llg='ls -l | grep'
alias hg='history | grep'
alias mlease='cs ~/Dropbox/MLease'
alias django='cs ~/Dropbox/JRo/Django'
alias vimrc='mvim ~/.vimrc'
alias bprof='mvim ~/.bash_profile'
alias this='export PATH="${PATH}:."'
alias sb='/Applications/Sublime\ Text\ 2.app/Contents/SharedSupport/bin/subl -n $@'
alias ut='ssh -o ServerAliveInterval=30 ethanp@charity.cs.utexas.edu'
alias utx='ssh -o ServerAliveInterval=10 -X ethanp@charity.cs.utexas.edu'

# download movie to Movies dir
function dlmov {
    cd ~/Desktop/Movies && youtube-dl -t $1 && cd -
}

function json {
   cat $1 | python -mjson.tool | less
}

# change & show
function cs {
    cd $1; ls
} # this isn't allowed to be compacted into the line above

# as in Does Directory Have... ( Case insensitive bc -i )
function ddh {
    ls $1 | grep -i $2
}

# delete directory
function dedir {
    if test $# -eq 0
    then
        echo "You must supply a directory to demolish"
    else
        if test -d "$1"
        then
            tree $1
            echo "Enter 1 to remove ${1} from the face of the Earth: "
            read response
            if [ $response -ne 1 ]
            then
                echo "Action Cancelled"
            elif [ $response -eq 1 ]
            then
                rm -fR $1
                echo "Action Completed"
            fi
        else
            echo "$1 is not a directory."
        fi
    fi
}

# There are almost Too Many ways this could be altered to make it even more
# useful (and "General"). But there's no reason to do them, unless I find
# myself using 'C' a lot, which I have not.

# NOTE: I've removed -O2 & -ffast-math optimization because there are going to
# be more situations where I don't want something optimized out than where I
# really care about the speed of program execution.

function compile {
    if test $# -gt 2
    then
        echo "You'll have to implement a better compile function to get that to work"
        echo "It can either use a for loop, or it can match *.c & *.h"

    elif test $# -eq 1
    then
        if test -f "$1"
        then
            echo "Your output is in a.out"
            # I took out -Wstrict-prototypes -Wmissing-prototypes
            gcc -W -Wall -fno-common -Wcast-align -std=c99 -Wredundant-decls -Wbad-function-cast -Wwrite-strings -Waggregate-return $1
        else
            echo "Usage: compile <inName> (<outName>)?"
            echo "In your case, <inName> wasn't a file."
        fi
    elif test $# -eq 0
    then
        echo "This is a shortcut for compiling simple .c files with a whole lot of warnings enabled"
        echo "Usage: compile <inName> (<outName>)?"
    else
        if [ -f "$1" ]
        then
            # I took out -Wstrict-prototypes -Wmissing-prototypes
            gcc -W -Wall -fno-common -std=c99 -Wcast-align -Wredundant-decls -Wbad-function-cast -Wwrite-strings -Waggregate-return $1 -o $2
        else
            echo "Usage: compile <inName> (<outName>)?"
            echo "In your case, <inName> wasn't a file."
        fi
    fi
}

PATH="/Library/Frameworks/Python.framework/Versions/2.7/bin:${PATH}"
# PATH="/Library/Frameworks/Python.framework/Versions/3.3/bin:${PATH}" # can switch to new Python
PATH="/Users/Ethan/Applications/javacc-5.0/bin:${PATH}"
PATH="/Users/Ethan/Applications/apache-ant-1.8.4/bin:${PATH}"
PATH="/lusr/opt/pintos/:/lusr/opt/bochs-2.2.6-pintos/bin/:/lusr/opt/qemu-1.1.1/:$PATH"
PATH="$PATH":~/Dropbox/CSyStuff/Google_depot_tools_git/depot_tools
PATH="$PATH":/usr/local/share/scala-2.10.1/bin
PATH=${PATH}:$HOME/gsutil
PATH="/usr/local/lib/ruby:${PATH}"
PATH=$PATH:$HOME/.rvm/bin # Add RVM to PATH for scripting
PATH="/usr/local/bin:${PATH}"
export PATH

# These are the places the "cd" command will LOOK, in this order too
CDPATH="::" # This means the current directory
CDPATH="${CDPATH}:$HOME" # HOME is a Global Var set as /Users/Ethan
CDPATH="${CDPATH}:${HOME}/Dropbox"
export CDPATH

# These are only saved _this_ session
HISTSIZE=2000
# These are saved between sessions in .bash_history
HISTFILESIZE=2000

#JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home
#export JAVA_HOME

# 6/18/13
# from http://jeethurao.com/blog/?p=217 for getting Scala to work with Intellij
# (I don't think it made a difference, I'm still "using an external compiler")
export JAVA_HOME=$(/usr/libexec/java_home)
export SCALA_HOME=/usr/local/Cellar/scala/2.10.0/libexec
export JAVACMD=drip
export DRIP_SHUTDOWN=30
export SBT_OPTS="-XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:PermSize=128M -XX:MaxPermSize=512M"

# tells java where to look for classes referenced by your program
# e.g: import my.package.Foo
CLASSPATH=.:/usr/share/java/commons-math3-3.2
export CLASSPATH

# I have set PATH for Python 2.7
# The orginal version is saved in .oldBashShit
# PYTHONPATH is the place where Python looks for user-defined MODULES
#       after searching the current directory
#   Here's what it looks like to add to it:
#       SOURCE: http://stackoverflow.com/questions/3387695/add-to-python-path-mac-os-x
# PYTHONPATH="/Me/Documents/mydir:$PYTHONPATH"
# export PYTHONPATH

# 6/18/13:
# Got rid of a bunch of terminal color definitions, and
# functions called 'mychkquota' and 'duf'
# I put them in ~/.oldBashShit, in case I miss them later on

##############################################################################
# The Cool Terminal That Guy from Piazza Found (Modified to taste) ##########
# Modified from  https://bbs.archlinux.org/viewtopic.php?pid=1068202#p1068202
# All 0 length commands should be matched with \[ and \]
NC='\033[0m'              # No Color
yellow='\033[0;33m'
green='\033[0;32m'
bright_cyan='\033[0;96m'
white='\033[0;37m'
red='\033[0;31m'
PROMPT_COMMAND="let PREV_ERROR=\$?"
PROMPT_TITLE='\033]0;${USER}@${HOSTNAME}:${DIRSTACK##*/}\007'
ROOT_NAME="[\[${red}\]${HOSTNAME}"
#USER_NAME="[\[${yellow}\]${USER}\[${white}\]@\[${bright_cyan}\]${HOSTNAME}"
USER_NAME="[\[${yellow}\]\[\[${bright_cyan}\]${HOSTNAME}"
PROMPT_ERROR="echo \"[\[${red}\]\342\234\227\[${white}\]]\342\224\200\")"
PRE_PROMPT="\[${white}\]\342\224\214\342\224\200\$([[ \$? != 0 ]] && ${PROMPT_ERROR}"
PROMPT="\[${white}\]\342\224\200[\[${green}\]\${DIRSTACK}\[${white}\]]\$(promptFill)"
#PROMPT="\[${white}\]]\342\224\200[\[${green}\]\${DIRSTACK}\[${white}\]]\$(promptFill)"
POST_PROMPT="\n\[${white}\]\342\224\224\342\224\200\342\224\200\342\225\274 \[${NC}\]"
# This would go between PRE_PROMPT and PROMPT, below
#$(if [[ ${EUID} == 0 ]];
#then echo "${ROOT_NAME}";
#else echo "${USER_NAME}"; fi)\

# PS1 is what the user prompt looks like. You know, that thing that's something
# like ${USER_EMAIL}_${CURRENT_DIR}\$ " The \ at the end of the line means that
# the next line is interpretted as part of the current line It basically
# escapes the \n character

PS1="${PROMPT_TITLE}${PRE_PROMPT}${PROMPT}${POST_PROMPT}"

function promptFill {
    NOW=$(date +"[%l:%M %p ]")
    #local string="xx[${USER}@${HOSTNAME}]-[${DIRSTACK}]"
    #local string="xx[${HOSTNAME}]-[${DIRSTACK}]"
    #local string="xx-[${DIRSTACK}]"
    if [[ $PREV_ERROR != 0 ]]; then
        #local string="xx[x]-[${HOSTNAME}]-[${DIRSTACK}]"
        local string="xx[x]--[${DIRSTACK}]"
    fi
    let length=${#string}+${#NOW}
    #for ((n=0;n<$(($COLUMNS-$length));n++))
    #do
        #   echo -ne " ";
    #done
    echo -ne "--"
    echo -n $NOW
    return $PREV_ERROR
}
### End Cool Terminal #########################################################
###############################################################################

# Load RVM into a shell session *as a function*
[[ -s "$HOME/.rvm/scripts/rvm" ]] && source "$HOME/.rvm/scripts/rvm"

