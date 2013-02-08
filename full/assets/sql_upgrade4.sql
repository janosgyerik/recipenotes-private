CREATE TABLE "recipes_recipe" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(80) NULL,
    "summary" text NULL,
    "display_name" text NULL,
    "display_image" varchar(80) NULL,
    "display_order" integer NULL,
    "memo" text NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "recipes_ingredient" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(80) NULL UNIQUE,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "recipes_unit" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "short_name" varchar(10) NULL UNIQUE,
    "long_name" varchar(20) NULL UNIQUE,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "recipes_recipeingredient" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL REFERENCES "recipes_recipe" ("_id"),
    "ingredient_id" integer NULL REFERENCES "recipes_ingredient" ("_id"),
    "amount" real NULL,
    "unit_id" integer REFERENCES "recipes_unit" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL,
    UNIQUE ("recipe_id", "ingredient_id")
)
;
CREATE TABLE "recipes_tag" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(40) NULL UNIQUE,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "recipes_recipetag" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL REFERENCES "recipes_recipe" ("_id"),
    "tag_id" integer NULL REFERENCES "recipes_tag" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL,
    UNIQUE ("recipe_id", "tag_id")
)
;
CREATE TABLE "recipes_recipestep" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL REFERENCES "recipes_recipe" ("_id"),
    "step" text NULL,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL,
    UNIQUE ("recipe_id", "step")
)
;
CREATE TABLE "recipes_recipephoto" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL REFERENCES "recipes_recipe" ("_id"),
    "filename" varchar(50) NULL,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL,
    UNIQUE ("recipe_id", "filename")
)
;
CREATE TABLE "recipes_favoriterecipe" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL UNIQUE REFERENCES "recipes_recipe" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "recipes_favoriteingredient" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "ingredient_id" integer NULL UNIQUE REFERENCES "recipes_ingredient" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "recipes_favoriterecipephoto" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_photo_id" integer NULL UNIQUE REFERENCES "recipes_recipephoto" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "recipes_favoritetag" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "tag_id" integer NULL UNIQUE REFERENCES "recipes_tag" ("_id"),
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
INSERT INTO recipes_recipe SELECT _id, name, summary, display_name, display_image, display_order, memo, created_dt, updated_dt FROM main_recipe;
INSERT INTO recipes_ingredient SELECT _id, name, display_order, created_dt, updated_dt FROM main_ingredient;
INSERT INTO recipes_recipeingredient SELECT _id, recipe_id, ingredient_id, amount, NULL, display_order, created_dt, updated_dt FROM main_recipeingredient;
INSERT INTO recipes_tag SELECT _id, name, display_order, created_dt, updated_dt FROM main_tag;
INSERT INTO recipes_recipetag SELECT _id, recipe_id, tag_id, display_order, created_dt, updated_dt FROM main_recipetag;
INSERT INTO recipes_recipephoto SELECT _id, recipe_id, filename, display_order, created_dt, updated_dt FROM main_recipephoto;
