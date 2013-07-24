"Numbers
set number

" Increase the spacing between lines for better readability
set linespace=4
" Auto-Indent By Default
set autoindent
set smartindent

" round indent to multiple of 'shiftwidth'
set shiftround

" Tab Size 4
set shiftwidth=4
set softtabstop=4
set tabstop=4

" fontsize
set guifont=Monaco:h18

"make tabs into spaces
set expandtab

" HighLight Search
set hlsearch

"Remove Toolbar
set guioptions-=T

" incremental search
set incsearch

" Show command you're typing
set showcmd

" Swap : and :  Convenient.
nnoremap ; :
nnoremap : ;

"Turn off Bell
set noerrorbells
set visualbell
set t_vb=

"Wrap cursor around lines
inoremap <Down> <C-o>gj
inoremap <Up> <C-o>gk
set whichwrap+=<,>,h,l,[,]

"Automatically cd into the directory that the file is in:
set autochdir

"Highlight current line
set cul
hi CursorLine term=none cterm=none ctermbg=3

"Background
"set background=dark

"Line number on bar
set ruler

"fileType detection
filetype on
filetype indent on

" keep 5 lines around cursor
set scrolloff=5

" show invisible characters
" set list
" but only show tabs and trailing whitespace
" set listchars=tab:>·,trail:

" toggle highlight search
nnoremap <backspace> :set hlsearch!<CR>

" change to jellybeans colors
nnoremap <F6> :colorscheme wombat<CR>
" change to pablo colors
nnoremap <F5> :colorscheme my_colour_scheme<CR>
" change to molokai colors
nnoremap <F7> :colorscheme molokai<CR>

" open the ~/.vimrc file in a new buffer
nnoremap <F3> :e ~/.vimrc<CR>

au! BufNewFile,BufRead *.indri set filetype=XML
au! BufNewFile,BufRead *.param set filetype=XML
au! BufNewFile,BufRead *.trectext set filetype=XML
au! BufNewFile,BufRead *.trec set filetype=XML

" set EasyMotion Plugin to <Leader> instead of <Leader><Leader>
let g:EasyMotion_leader_key = '<Leader>'

" makes wrapped lines at spaces, not middle of words
set linebreak

" makes Vim put all its backup and temporary files in places I don't mind:
set backupdir=~/.vim/vim-tmp,~/.tmp,~/tmp,~/var/tmp,/tmp
set directory=~/.vim/vim-tmp,~/.tmp,~/tmp,~/var/tmp,/tmp

" allow "fj" instead to exit to command mode
inoremap fj <esc>

" Type before comment to keep it in same indentation in Python
inoremap  <tab><backspace>

" set "fk" to save in normal mode
map fk ;w<CR>

" DOESN'T WORK \/!
" set "fl" to exit command mode and save
" inoremap fl <esc>;w

" Smart way to move between windows
map <C-j> <C-W>j
map <C-k> <C-W>k
map <C-h> <C-W>h
map <C-l> <C-W>l

" DOESN"T SEEM TO WORK :(
" Move a line of text using ALT+[jk] or Comamnd+[jk] on mac
nmap <M-j> mz:m+<cr>`z
nmap <M-k> mz:m-2<cr>`z
vmap <M-j> :m'>+<cr>`<my`>mzgv`yo`z
vmap <M-k> :m'<-2<cr>`>my`<mzgv`yo`z
if has("mac") || has("macunix")
    nmap <D-j> <M-j>
    nmap <D-k> <M-k>
    vmap <D-j> <M-j>
    vmap <D-k> <M-k>
endif


" Change "\" to "g"
" map \ g
" noremap g \
noremap \ gg
let mapleader = "g"

" turn on plugins?
filetype plugin on

call pathogen#infect()
syntax on
filetype plugin indent on

nnoremap zx :NERDTree<CR>

" save sessions with .vis extension
map <leader>s :mksession!  session.vis<CR>

" Insert-blank-line buttons
map <leader>io Ofjj
map <leader>o ofjk

" automatically source vim sessions so I can open them with the finder
au BufRead *.vis so %

" give current file execute permissions
map <leader>h ;!chmod a+x %<CR><CR>


" simplify buffer switches
map <F13> <ESC>;bp<CR>
map <F14> <ESC>;bn<CR>

nnoremap j gj
nnoremap k gk

" Give the Undo Tree easy access
nnoremap <Leader>z :GundoToggle<CR>

let g:indent_guides_start_level = 2
let g:indent_guides_guide_size = 1

colorscheme my_colour_scheme

" uses gig to get indent guides

" Instantly reload vimrc. 'Been waiting for this one...
nnoremap <leader>vr ;source $MYVIMRC<CR>

nnoremap zc :CommandT ~/Dropbox/<CR>

let g:rainbow_active = 1
let g:rainbow_operators = 1

" make tab switching real easy (esp. using mouse/trackpad)
map <M-D-Left> <ESC>;tabp<CR>
map <M-D-Right> <ESC>;tabn<CR>
