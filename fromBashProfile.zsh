# Add yourself some shortcuts to projects you often work on
# Example:
#
# brainstormr=/Users/Ethan/Projects/development/planetargon/brainstormr

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
alias trracker='cs ~/Dropbox/CSyStuff/TTRails/trracker'
alias play_activator='~/Applications/activator-1.0.10/activator ui'
alias blog='cs ~/code/personal_project_use/libraries_to_use/Ruby/octopress/'
PROGRAMMINGGIT=~/Dropbox/CSyStuff/ProgrammingGit
alias ca='cs $PROGRAMMINGGIT/StuffIWrote/Scala/CommentAnalyzer/CommentAnalyzer_0'
alias playakka='cs $PROGRAMMINGGIT/StuffIWrote/Scala/akka-redis-websockets-play-scala_translation'
alias notes='cs $PROGRAMMINGGIT/Notes'
alias empat='ssh ethan@empat.csres.utexas.edu' # AOS class beastly Linux machine
# R: Let `less` use colors,
# F: Just `cat` if it's only one screen worth of info
# X: "Disables sending the termcap initialization strings to the terminal." (?)
export LESS=-RFX
# render manpage as postscript in Preview
function pman { man -t $1 | open -fa /Applications/Preview.app ; }
# download movie to Movies dir
function dlmov { cd ~/Desktop/Movies/ && youtube-dl -t $1  && cd - ; }

# change & show
function cs { cd "$1"; ls; }

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

# 1/12/15
# This was edited from apple.stackexchange.com/questions/4240
# It makes it so that you can open the app My App Name from the command-line
# using
#   $ MyAppName
# and you can use My App Name to open myfile.txt using
#   $ MyAppName myfile.txt
# I got it so that Skim can open generated Latex PDFs with
#   $ Skim Java\ Notes.pdf
# in the `mtx` command above.
#
# How it works:
# 1. remove an existing clashing alias dir if there is one
# 2. find all the .app's in the /Applications/ directory
# 3. remove spaces and single-quotes for the alias-version of the command
# 4. ${varible%pattern} deletes the *shortest* match from the *end* of the
#    varname and returns the rest, so ${a%.*} removes from the last period on,
#    turning Skim.app/ into Skim
function openApp() {
    rm -f ~/.openApp.tmp
    ls /Applications/ | grep "\.app" | while read APP; do
        a=`echo $APP | sed -e s/\ //g -e s/\'//g`;
        echo alias ${a%.*}="'open -a \"${APP%.*}\"'" >> ~/.openApp.tmp
    done
    source ~/.openApp.tmp
    rm -f ~/.openApp.tmp
}
openApp


# 5/27/14
# open mmd as pdf
function mtx {
    TEX_NAME=$(basename "$1" | sed s'|.md|.tex|')
    PDF_NAME=$(basename "$1" | sed s'|.md|.pdf|')
    LATEX_DIR=~/Desktop/Latex
    TEX_LOC=$LATEX_DIR/"$TEX_NAME"
    multimarkdown -t latex "$1" > "$TEX_LOC"
    pdflatex --output-directory "$LATEX_DIR" "$TEX_LOC" > /dev/null
    # open -a /Applications/Preview.app "$LATEX_DIR"/"$PDF_NAME"
    Skim "$LATEX_DIR"/"$PDF_NAME" # requires openApp to have run already
}

# These are only saved _this_ session
HISTSIZE=2000

# These are saved _between_ sessions in .bash_history
HISTFILESIZE=10000

# Brew tab-completion (works) [https://github.com/miku/brew-completion]
if [ -f ~/code/brew-completion/brew-completion.sh ]; then
    . ~/code/brew-completion/brew-completion.sh
fi
