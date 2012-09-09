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
