src_dir:=src
hdpi_dir:=../res/drawable-hdpi
ldpi_dir:=../res/drawable-ldpi
mdpi_dir:=../res/drawable-mdpi

names:=$(patsubst $(src_dir)/%,%,$(wildcard $(src_dir)/*.png $(src_dir)/*.jpg))
hdpi_target:=$(patsubst %,$(hdpi_dir)/%,$(names))
ldpi_target:=$(patsubst %,$(ldpi_dir)/%,$(names))
mdpi_target:=$(patsubst %,$(mdpi_dir)/%,$(names))
launchpad_target:=launchpad/icon.png launchpad/logo.png launchpad/brand.png
market_target:=market/android.png
feature_target:=market/feature.png
canvas:=market/canvas.png
work:=market/work.png

default: all

help:
	@cat Makefile.readme

hdpi: $(hdpi_target)

ldpi: $(ldpi_target)

mdpi: $(mdpi_target)

lp launchpad: $(launchpad_target)

market: $(market_target)

feature: $(feature_target)

all: hdpi ldpi mdpi launchpad market feature

clean: 
	rm $(hdpi_target) $(ldpi_target) $(mdpi_target) $(launchpad_target) $(market_target) $(feature_target)

$(hdpi_dir)/tab_%.png: $(src_dir)/tab_%.png
	convert -geometry 48x $< $@
	identify $@

$(ldpi_dir)/tab_%.png: $(src_dir)/tab_%.png
	convert -geometry 32x $< $@
	identify $@

$(mdpi_dir)/tab_%.png: $(src_dir)/tab_%.png
	convert -geometry 40x $< $@
	identify $@

$(hdpi_dir)/menu_%.png: $(src_dir)/menu_%.png
	convert -geometry 72x $< $@
	identify $@

$(ldpi_dir)/menu_%.png: $(src_dir)/menu_%.png
	convert -geometry 36x $< $@
	identify $@

$(mdpi_dir)/menu_%.png: $(src_dir)/menu_%.png
	convert -geometry 48x $< $@
	identify $@

$(hdpi_dir)/btn_%.png: $(src_dir)/btn_%.png
	convert -geometry 48x $< $@
	identify $@

$(ldpi_dir)/btn_%.png: $(src_dir)/btn_%.png
	convert -geometry 24x $< $@
	identify $@

$(mdpi_dir)/btn_%.png: $(src_dir)/btn_%.png
	convert -geometry 36x $< $@
	identify $@

$(hdpi_dir)/headerbtn_%.png: $(src_dir)/headerbtn_%.png
	convert -geometry 36x $< $@
	identify $@

$(ldpi_dir)/headerbtn_%.png: $(src_dir)/headerbtn_%.png
	convert -geometry 18x $< $@
	identify $@

$(mdpi_dir)/headerbtn_%.png: $(src_dir)/headerbtn_%.png
	convert -geometry 24x $< $@
	identify $@

$(hdpi_dir)/launcher_%.png: $(src_dir)/launcher_%.png
	convert -geometry 72x $< $@
	identify $@

$(ldpi_dir)/launcher_%.png: $(src_dir)/launcher_%.png
	convert -geometry 36x $< $@
	identify $@

$(mdpi_dir)/launcher_%.png: $(src_dir)/launcher_%.png
	convert -geometry 48x $< $@
	identify $@

launchpad/icon.png: $(src_dir)/launcher_main.png
	@mkdir -p $(@D)
	convert -geometry 14x $< $@
	identify $@

launchpad/logo.png: $(src_dir)/launcher_main.png
	@mkdir -p $(@D)
	convert -geometry 64x $< $@
	identify $@

launchpad/brand.png: $(src_dir)/launcher_main.png
	@mkdir -p $(@D)
	convert -geometry 192x $< $@
	identify $@

market/android.png: $(src_dir)/launcher_main.png
	@mkdir -p $(@D)
	convert -geometry 512x $< $@
	identify $@

gource: gource/gource.webm

gource/gource.webm: gource/gource.ppm
	ffmpeg -y -r 60 -f image2pipe -vcodec ppm -i $< -vcodec libvpx -b 10000K $@
	> $<
	touch $@
	rm -f opsnr.stt

gource/gource.ppm:
	mkdir -p gource
	gource --file-idle-time 0 --seconds-per-day .5 -640x480 -o $@


market/feature.png: $(src_dir)/launcher_main.png
	@mkdir -p $(@D)
	convert -size 1024x500 xc:transparent $(canvas)
	convert -geometry 1024x500 $< $(work)
	convert -composite $(canvas) $(work) -gravity west $@
	identify $@

