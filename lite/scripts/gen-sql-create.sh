#!/bin/sh

cd $(dirname "$0")

django_dir=../../django-recipenotes
db=$django_dir/sqlite3.db
sql=../assets/sql_create.sql

$django_dir/gen-sql-create.sh > $sql

sqlite3 $db '.dump recipes_ingredient' | grep INSERT >> $sql
sqlite3 $db '.dump recipes_tag' | grep INSERT >> $sql
sqlite3 $db '.dump recipes_recipe' | grep INSERT >> $sql
sqlite3 $db '.dump recipes_recipeingredient' | grep INSERT >> $sql
sqlite3 $db '.dump recipes_recipetag' | grep INSERT >> $sql

# workaround for older sqlite3 in android
cat <<EOF >> $sql
UPDATE recipes_ingredient SET created_dt = NULL;
UPDATE recipes_ingredient SET updated_dt = NULL;
UPDATE recipes_tag SET created_dt = NULL;
UPDATE recipes_tag SET updated_dt = NULL;
UPDATE recipes_recipe SET created_dt = NULL;
UPDATE recipes_recipe SET updated_dt = NULL;
UPDATE recipes_recipeingredient SET created_dt = NULL;
UPDATE recipes_recipeingredient SET updated_dt = NULL;
UPDATE recipes_recipetag SET created_dt = NULL;
UPDATE recipes_recipetag SET updated_dt = NULL;
EOF

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
