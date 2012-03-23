CREATE TABLE "main_wine" (
    "_id" integer auto increment PRIMARY KEY,
    "wine_type" varchar(20) NULL,
    "name" varchar(80) NULL,
    "year" integer NULL,
    "region" varchar(80) NULL,
    "grape" varchar(80) NULL,
    "description" text NULL,
    "note" text NULL,
    "aroma" integer NULL,
    "taste" integer NULL,
    "after_taste" integer NULL,
    "overall" integer NULL,
    "to_buy" varchar(1) NULL
)
;
CREATE TABLE "main_tag" (
    "_id" integer auto increment PRIMARY KEY,
    "name" varchar(50) NULL
)
;
CREATE TABLE "main_aromatag" (
    "tag_ptr_id" integer auto increment PRIMARY KEY REFERENCES "main_tag" ("_id")
)
;
CREATE TABLE "main_tastetag" (
    "tag_ptr_id" integer auto increment PRIMARY KEY REFERENCES "main_tag" ("_id")
)
;
CREATE TABLE "main_winetag" (
    "_id" integer auto increment PRIMARY KEY,
    "wine_id" integer NULL REFERENCES "main_wine" ("_id"),
    "tag_id" integer NULL REFERENCES "main_aromatag" ("tag_ptr_id")
)
;
