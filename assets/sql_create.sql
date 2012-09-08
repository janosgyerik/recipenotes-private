CREATE TABLE "main_recipe" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(80) NULL,
    "summary" text NULL,
    "display_name" text NULL,
    "display_image" varchar(80) NULL,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_ingredient" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(80) NULL UNIQUE,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_unit" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "short_name" varchar(10) NULL UNIQUE,
    "long_name" varchar(20) NULL UNIQUE,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_recipeingredient" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL REFERENCES "main_recipe" ("_id"),
    "ingredient_id" integer NULL REFERENCES "main_ingredient" ("_id"),
    "amount" real NULL,
    "unit_id" integer NULL REFERENCES "main_unit" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL,
    UNIQUE ("recipe_id", "ingredient_id")
)
;
CREATE TABLE "main_tag" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(40) NULL UNIQUE,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_recipetag" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL REFERENCES "main_recipe" ("_id"),
    "tag_id" integer NULL REFERENCES "main_tag" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL,
    UNIQUE ("recipe_id", "tag_id")
)
;
CREATE TABLE "main_recipestep" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL REFERENCES "main_recipe" ("_id"),
    "step" text NULL,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL,
    UNIQUE ("recipe_id", "step")
)
;
CREATE TABLE "main_recipephoto" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL REFERENCES "main_recipe" ("_id"),
    "filename" varchar(50) NULL,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL,
    UNIQUE ("recipe_id", "filename")
)
;
CREATE TABLE "main_favoriterecipe" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL UNIQUE REFERENCES "main_recipe" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_favoriteingredient" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "ingredient_id" integer NULL UNIQUE REFERENCES "main_ingredient" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_favoriterecipephoto" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_photo_id" integer NULL UNIQUE REFERENCES "main_recipephoto" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_favoritetag" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "tag_id" integer NULL UNIQUE REFERENCES "main_tag" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
