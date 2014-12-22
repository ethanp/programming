latex input:		mmd-article-header
Title:		ZSHell Notes
Author:		Ethan C. Petuchowski
Base Header Level:		1
latex mode:		memoir
Keywords:		Bash, Unix, Linux, Shell, Command Line, Terminal, Syntax
CSS:		http://fletcherpenney.net/css/document.css
xhtml header:		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
</script>
copyright:			2014 Ethan C. Petuchowski
latex input:		mmd-natbib-plain
latex input:		mmd-article-begin-doc
latex footer:		mmd-memoir-footer

# Z-Shell

## Refs

1. [https://wiki.archlinux.org/index.php/zsh]()
2. [https://github.com/robbyrussell/oh-my-zsh](), [http://ohmyz.sh/]()
3. [http://zanshin.net/2013/09/03/how-to-use-homebrew-zsh-instead-of-max-os-x-default/]()
4. [http://www.iterm2.com/#/section/features/configurability]()
5. [http://mikebuss.com/2014/02/02/a-beautiful-productive-terminal-experience/]()
6. [http://mikegrouchy.com/blog/2012/01/zsh-is-your-friend.html]()
7. [http://stevelosh.com/blog/2010/02/my-extravagant-zsh-prompt/]()
8. [http://www.paradox.io/posts/9-my-new-zsh-prompt]()

### Some Features I've Found

1. I have installed [Zsh-fuzzy-match][], which means that by pressing `cmd-t`,
   then it does a `find .` and then you interactively choose the file you want
   to open
3. You can `google multiple words` to Google it.
4. The `rails` plugin gives you
    1. `rc`, `rs`, `rg='rails generate'`, `rgm='rails generate migration'`,
       `rdm='rake db:migrate`

### TODO

1. Look into the [common-aliases plugin][] to see what I got. There's some
   real cool ideas in there
2. Look into the `compleat` plugin --- the point is to make it easy to make
   your own autocomplete setups. This sounds like a good idea.
3. Probably worth using `fasd` plugin *instead of* `autojump`
4. Why isn't the `dirhistory` plugin working?

[common-aliases plugin]: /Users/Ethan/.oh-my-zsh/plugins/common-aliases/common-aliases.plugin.zsh

[Zsh-fuzzy-match]: https://github.com/tarruda/zsh-fuzzy-match
