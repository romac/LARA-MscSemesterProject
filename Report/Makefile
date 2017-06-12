
.PHONY: all clean commit

all: report.pdf

%.pdf: %.md bibliography.bib
	pandoc -s --latex-engine=xelatex -N \
	 --bibliography=bibliography.bib \
	 --csl=acm-sig-proceedings.csl \
	 $< -o $@

%.tex: %.md bibliography.bib
	pandoc -s $< -o $@

clean:
	rm -f report.pdf

commit:
	git add report.md
	git commit -m "Update semester project report"

