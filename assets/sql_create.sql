CREATE TABLE "main_recipe" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(80) NULL,
    "summary" text NULL,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_ingredient" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(80) NULL,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_unit" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "short_name" varchar(10) NULL,
    "long_name" varchar(20) NULL,
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
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_tag" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "name" varchar(40) NULL,
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
    "updated_dt" datetime NULL
)
;
CREATE TABLE "main_recipestep" (
    "_id" integer NULL PRIMARY KEY AUTOINCREMENT,
    "recipe_id" integer NULL REFERENCES "main_recipe" ("_id"),
    "step" text NULL,
    "display_order" integer NULL,
    "created_dt" datetime NULL,
    "updated_dt" datetime NULL
)
;
