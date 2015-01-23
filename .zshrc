# EP: note all the custom functions and aliases are in
# ~/.oh-my-zsh/custom/fromBashProfile.zsh
# (loaded because that's ZSH_CUSTON=$ZSH/custom)

# Path to your oh-my-zsh installation.
export ZSH=$HOME/.oh-my-zsh

# Set name of the theme to load.
# Look in ~/.oh-my-zsh/themes/
# You can also choose "random"
ZSH_THEME="amuse"

# case-sensitive completion.
# CASE_SENSITIVE="true"

# bi-weekly auto-update checks.
# DISABLE_AUTO_UPDATE="true"

# how often to auto-update (in days).
# export UPDATE_ZSH_DAYS=13

# DISABLE_LS_COLORS="true"

# auto-setting terminal title.
# DISABLE_AUTO_TITLE="true"

# enable command auto-correction.
ENABLE_CORRECTION="true"

# display red dots whilst waiting for completion.
# COMPLETION_WAITING_DOTS="true"

# disable marking untracked files under VCS as dirty. This makes repository
# status check for large repositories much, much faster.
# DISABLE_UNTRACKED_FILES_DIRTY="true"

# Uncomment the following line if you want to change the command execution time
# stamp shown in the history command output.
# The optional three formats: "mm/dd/yyyy"|"dd.mm.yyyy"|"yyyy-mm-dd"
# HIST_STAMPS="mm/dd/yyyy"

# Would you like to use another custom folder than $ZSH/custom?
# ZSH_CUSTOM=/path/to/new-custom-folder

# Which plugins would you like to load? (plugins can be found in ~/.oh-my-zsh/plugins/*)
# Custom plugins may be added to ~/.oh-my-zsh/custom/plugins/
# Example format: plugins=(rails git textmate ruby lighthouse)
# Add wisely, as too many plugins slow down shell startup.
plugins=(git rails ruby brew common-aliases web-search z rails)

# User configuration

PATH+="/usr/local/bin:"
PATH+="/Users/Ethan/code/personal_project_use/code_to_base_off_of/Ruby/fuzzycd:"
PATH+="/usr/local/Cellar/coreutils/8.23/libexec/gnubin:"
PATH+="/Applications/Postgres93.app/Contents/MacOS/bin:"
PATH+="/usr/local/lib/ruby:"
PATH+="/Applications/Anaconda/anaconda/bin:"
PATH+="/Users/Ethan/Applications/apache-ant-1.8.4/bin:"
PATH+="/Users/Ethan/Applications/javacc-5.0/bin:"
PATH+="/Users/Ethan/.rbenv/shims:"
PATH+="/usr/bin:"
PATH+="/bin:"
PATH+="/usr/sbin:"
PATH+="/sbin:"
PATH+="/opt/X11/bin:"
PATH+="/usr/texbin:"
PATH+="/Users/Ethan/Dropbox/CSyStuff/Google_depot_tools_git/depot_tools:"
PATH+="/usr/local/share/scala-2.10.1/bin:"
PATH+="/Users/Ethan/gsutil:"
PATH+="/Users/Ethan/.rvm/bin:"
PATH+="/Users/Ethan/.rvm/gems/ruby-2.1.2/bin"

export PATH
# export MANPATH="/usr/local/man:$MANPATH"

source $ZSH/oh-my-zsh.sh

# manually set your language environment
# export LANG=en_US.UTF-8

# Preferred editor for local and remote sessions
# if [[ -n $SSH_CONNECTION ]]; then
#   export EDITOR='vim'
# else
#   export EDITOR='mvim'
# fi

# Compilation flags
# export ARCHFLAGS="-arch x86_64"

# ssh
# export SSH_KEY_PATH="~/.ssh/dsa_id"

# Aliases can be placed here, though oh-my-zsh
# users are encouraged to define aliases within the ZSH_CUSTOM folder.
# For a full list of active aliases, run `alias`.
#
# Example aliases
# alias zshconfig="mate ~/.zshrc"
# alias ohmyzsh="mate ~/.oh-my-zsh"
source $HOME/.zsh-fuzzy-match/fuzzy-match.zsh
