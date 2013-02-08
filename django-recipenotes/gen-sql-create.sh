#!/bin/sh

cd $(dirname "$0")

./manage.py sql recipes | sed \
    -e 's/PRIMARY KEY/& AUTOINCREMENT/' \
    -e 's/NOT NULL/NULL/' \
    -e '/^BEGIN/d' \
    -e '/^COMMIT/d'

# eof
