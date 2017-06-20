
.PHONY: all clean commit watch

all: slides.pdf

%.pdf: %.md bibliography.bib
	pandoc -s --latex-engine=xelatex -N \
	 --bibliography=bibliography.bib \
	 -t beamer --slide-level 1 \
	 --csl=acm-sig-proceedings.csl \
	 $< -o $@

%.tex: %.md bibliography.bib
	pandoc -s $< -o $@

watch:
	fswatch -o . | xargs -n1 -I{} make

clean:
	rm -f slides.pdf

commit:
	git add slides.md
	git commit -m "Update semester project slides"

