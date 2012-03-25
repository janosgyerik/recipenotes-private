CREATE TABLE "main_wine" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(80) NULL,
    "year" integer NULL,
    "wine_type" varchar(20) NULL,
    "region" varchar(80) NULL,
    "grape" varchar(80) NULL,
    "description" text NULL,
    "note" text NULL,
    "aroma" integer NULL,
    "taste" integer NULL,
    "after_taste" integer NULL,
    "overall" integer NULL,
    "buy_flag" varchar(1) NULL
)
;
CREATE TABLE "main_tag" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(50) NULL
)
;
CREATE TABLE "main_aromatag" (
    "tag_ptr_id" integer NULL PRIMARY KEY AUTOINCREMENT REFERENCES "main_tag" ("_id")
)
;
CREATE TABLE "main_tastetag" (
    "tag_ptr_id" integer NULL PRIMARY KEY AUTOINCREMENT REFERENCES "main_tag" ("_id")
)
;
CREATE TABLE "main_winetag" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "wine_id" integer NULL REFERENCES "main_wine" ("_id"),
    "tag_id" integer NULL REFERENCES "main_aromatag" ("tag_ptr_id")
)
;
