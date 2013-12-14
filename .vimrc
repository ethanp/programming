
" INITIALS

call pathogen#infect()

noremap \ gg
let mapleader = ","     " using g for <leader> meant I couldn't then use the g commands

filetype on             "fileType detection
filetype indent on

syntax on
colorscheme my_colour_scheme


" =============================================================================

" GENERAL SETTINGS

set shiftwidth=4        " Tab Size 4
set softtabstop=4
set tabstop=4
set shiftround          " round indent to multiple of 'shiftwidth'
set guifont=Monaco:h18  " font/size
set expandtab           " make tabs into spaces
set hlsearch            " HighLight Search
set guioptions-=T       " Remove Toolbar
set incsearch           " incremental search
set showcmd             " Show command you're typing
set noerrorbells        " Turn off Bell
set visualbell
set t_vb=
set autochdir           " Automatically cd into the directory that the file is in:
set cul                 " Highlight current line
hi CursorLine term=none cterm=none ctermbg=3
set ruler               " Line number on bar
set linebreak           " Makes wrapped lines at spaces, not middle of words
set nocompatible        " Don't try to be compatible with vi
set linespace=4         " Increase the spacing between lines for better readability
set autoindent
set smartindent
set encoding=utf-8
set wildmenu
set wildmode=list:longest       " not sure what this does
set ttyfast                     " makes vim faster, I guess
set backspace=indent,eol,start  " Normalize backspace, I remapped it anyway though
set laststatus=2                " Always display the status line
" set relativenumber            " line numbers relative to current line (found this annoying)
set number                      " show normal line numbers
set undofile                    " save file's undos, load them on re-open
set gdefault                    " apply global substitutions by default
set colorcolumn=90
set clipboard=unnamed           " Share system clipboard
set directory=/tmp/             " swap files go here
set backupskip=/tmp/*,/private/tmp/*
set ffs=unix,dos,mac            "Default file types
set nowrap                      " don't wrap lines, let them trail off the side
set showmatch                   " set show matching parenthesis
set ignorecase                  " ignore case by default when searching
set smartcase   " ignore case if search pattern is all lowercase, case-sensitive otherwise
set copyindent                  " copy the previous indentation on autoindenting
set list listchars=tab:»\ ,trail:· " Characters to show spaces/tabs/etc.
" set scrolloff=5               " keep 5 lines around cursor (not a fan)

" =============================================================================

" REMAPS

" Assign ; -> : but DON'T go the other way
nnoremap ; :
" nnoremap : ;

" allow "fj" to exit to command mode
inoremap fj <esc>

" set "fk" to save in normal mode
map fk ;w<CR>

" toggle highlighting of items found matching search term
nnoremap <backspace> :set hlsearch!<CR>

" Simplified way to move between inner-windows
map <C-j> <C-W>j
map <C-k> <C-W>k
map <C-h> <C-W>h
map <C-l> <C-W>l

" Insert-blank-line
map <leader>io Ofjj
map <leader>o ofjk

" Simple colorscheme switching
nnoremap <F6> :colorscheme wombat<CR>
nnoremap <F5> :colorscheme my_colour_scheme<CR>
nnoremap <F7> :colorscheme molokai<CR>

" set EasyMotion Plugin to <Leader> instead of <Leader><Leader>
let g:EasyMotion_leader_key = '<Leader>'

" strip all trailing whitespace in the current file
"  I think that :let command says
"   'take each instance of the matched text and replace it with the emptystring'
nnoremap <leader>l :%s/\s\+$//<cr>:let @/=''<CR>

nnoremap <leader>a :Ack
nnoremap zx :NERDTree<CR>

" re-hardwrap paragraphs of text
nnoremap <leader>q gqip


" VIMRC STUFF

" open up ~/.vimrc file in a vertically split window
nnoremap <leader>vv <C-w><C-v><C-l>:e $MYVIMRC<cr>

" open up ~/.vimrc file in a horizontally split window
nnoremap <leader>vs <C-w><C-s><C-l>:e $MYVIMRC<cr>

" open the ~/.vimrc file in a new buffer
nnoremap <leader>vw :e ~/.vimrc<CR>

" Instantly reload vimrc. 'Been waiting for this one...
" TODO It's broken
" nnoremap <leader>vr :source $MYVIMRC<CR>

" save sessions with .vis extension
" TODO find out how to use this feature properly
map <leader>s :mksession!  session.vis<CR>

" give current file execute permissions
map <leader>h ;!chmod a+x %<CR><CR>

" simplify buffer switches on big keyboard
map <F13> <ESC>;bp<CR>
map <F14> <ESC>;bn<CR>

" Undo Tree
nnoremap <Leader>z :GundoToggle<CR>

" really simple and fast file-finder plugin
nnoremap zc :CommandT<CR>
nnoremap zC :CommandT ~/Dropbox/<CR>

" Assign tab switching to global norm (also for using mouse/trackpad)
map <M-D-Left> <ESC>;tabp<CR>
map <M-D-Right> <ESC>;tabn<CR>

" Wrap cursor around lines
inoremap <Down> <C-o>gj
inoremap <Up> <C-o>gk
set whichwrap+=<,>,h,l,[,]
nnoremap j gj
nnoremap k gk

" =============================================================================

" RANDOM CRAP

" highlight extra whitespace in blue
highlight ExtraWhitespace ctermbg=blue guibg=blue
match ExtraWhitespace /\s\+$/

" Save when losing focus
set autowriteall            " Auto-save files when switching buffers or leaving vim.
au FocusLost * silent! :wa
au TabLeave * silent! :wa

set backupdir=~/.vim/vim-tmp,~/.tmp,~/tmp,~/var/tmp,/tmp
set directory=~/.vim/vim-tmp,~/.tmp,~/tmp,~/var/tmp,/tmp

" for Indri files
au! BufNewFile,BufRead *.indri set filetype=XML
au! BufNewFile,BufRead *.param set filetype=XML
au! BufNewFile,BufRead *.trectext set filetype=XML
au! BufNewFile,BufRead *.trec set filetype=XML

" for Markdown files
au! BufNewFile,BufRead *.md set filetype=markdown

" automatically source vim sessions so I can open them with the finder
au BufRead *.vis so %

" turns RainbowParentheses on by default
let g:rainbow_active = 1
let g:rainbow_operators = 1

" SOME CREDITS/REFERENCES
"   * http://stevelosh.com/blog/2010/09/coming-home-to-vim/
"   * https://github.com/jasoncyu/dotfiles/blob/master/.vimrc
