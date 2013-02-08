#!/bin/sh

cd $(dirname "$0")

django_dir=../../django-recipenotes
db=$django_dir/sqlite3.db
sql=../assets/sql_create.sql

$django_dir/gen-sql-create.sh > $sql

sqlite3 $db '.dump main_ingredient' | grep INSERT >> $sql
sqlite3 $db '.dump main_tag' | grep INSERT >> $sql
sqlite3 $db '.dump main_recipe' | grep INSERT >> $sql
sqlite3 $db '.dump main_recipeingredient' | grep INSERT >> $sql
sqlite3 $db '.dump main_recipetag' | grep INSERT >> $sql

cat <<EOF
# How to connect to a sqlite database on Android
# Warning: this works only on Android DEV phones and jailbroken phones
# As a workaround, you can add an activity for debugging purposes which
# copies the database file to /sdcard.
#
$ adb -s emulator-5554 shell
# sqlite3 /data/data/com.example.google.rss.rssexample/databases/rssitems.db
SQLite version 3.3.12
Enter ".help" for instructions
.... enter commands, then quit...
sqlite> .exit 
EOF

# eof
