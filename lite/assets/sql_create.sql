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
INSERT INTO "recipes_ingredient" VALUES(1,'Avocado',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(2,'Walnuts',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(3,'Hazelnuts',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(4,'Olive oil',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(5,'Grapeseed oil',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(6,'Eggs',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(7,'Lemon',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(8,'Lime',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(9,'Steak',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(10,'Rice',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(11,'Potatoes',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(12,'Tomatoes',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(13,'Zucchini',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(14,'Asparagus',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(15,'Lettuce',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(16,'Almond',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(18,'Pepper',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(19,'Salt',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(20,'Plum tomatoes',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(21,'Cherry tomatoes',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(22,'Pork',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(23,'Chicken',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(24,'Chicken breast',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(25,'Soup stock',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(26,'Spinach',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(27,'Onion',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(28,'Beet',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(29,'Red radish',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(30,'White radish',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(31,'Wasabi',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(32,'Goya',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(33,'Tofu',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(34,'Carrot',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(35,'Couscous',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(36,'Fish',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(37,'Garlic',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(38,'Basil paste',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(39,'Fresh basil',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(41,'Laurel',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(42,'Coconut milk',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(43,'Coconut',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(44,'Brown sugar',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(45,'White sugar',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(46,'Sea salt',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(47,'Milk',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(48,'Honey',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(49,'Cucumber',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(50,'Cauliflower',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(51,'Flour',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(52,'Parmesan cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(53,'Gruyere cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(54,'Rochefort cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(55,'Reblochon cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(56,'Peanuts',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(57,'Pine nuts',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(58,'Acacia honey',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(59,'Minced meat',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(60,'Cottage cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(61,'Black pepper',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(62,'Soy sauce',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(63,'Curry powder',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(64,'Linguine',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(65,'Champignon mushrooms',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(66,'Cream',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(67,'Ruccola',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(68,'Langousts',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(69,'Coconut butter',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(70,'Cabillaud',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(71,'Celery',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(72,'Black olives',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(74,'Mint',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(75,'Mashed potato',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(76,'Red chili pepper',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(77,'Pesto',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(78,'Tomato paste',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(79,'Yoghurt',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(80,'Green beans',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(81,'Mustard',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(88,'Feta cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(89,'Horseradish',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(90,'Capers',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(91,'Goat cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(92,'Tomato sauce',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(93,'Thyme',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(94,'Fresh black pepper',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(95,'Cream cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(96,'Bread',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(97,'Chanterelle mushrooms',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(98,'Butter',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(100,'Saffron',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(101,'Coriander',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(102,'Meat loaf',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(103,'Sun-dried tomatoes',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(104,'Fusilli',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(105,'Provencal spices',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(106,'Paprika',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(107,'Dill',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(108,'Cepe mushrooms',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(109,'Tagliatelle',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(110,'Pickled cucumber',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(111,'Veal',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(112,'Whole-grain fusilli',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(113,'Brussels sprouts',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(114,'Chickpeas',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(115,'Sesame seeds',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(118,'Anchovies',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(119,'Apple vinegar',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(120,'Bacon',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(121,'Baking powder',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(122,'Balsamic vinegar',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(123,'Beef',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(124,'Beer',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(125,'Cabbage',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(126,'Cashew nuts',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(128,'Chili pepper',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(129,'Chipolatas',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(130,'Cloves',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(131,'Croutons',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(132,'Cumin',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(133,'Curry paste',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(134,'Fettucine',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(135,'Fresh coriander',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(136,'Fresh cream',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(137,'Fresh dill',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(138,'Fresh mint',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(139,'Fresh parsley',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(140,'Fried tofu',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(141,'Ginger',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(142,'Gnocchi',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(143,'Grapefruit',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(144,'Green bell pepper',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(145,'Green olives',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(146,'Green peas',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(147,'Ground mustard',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(148,'Ham',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(149,'Icecream',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(150,'Jam',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(151,'Lamb',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(152,'Lasagna sheets',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(153,'Leek',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(154,'Lime leaves',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(155,'Mandarins',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(156,'Mascarpone',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(157,'Melon',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(158,'Mozzarella cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(159,'Mushrooms',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(160,'Navy beans',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(161,'Oat meal',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(162,'Orange juice',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(163,'Oregano',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(164,'Pappardelle',50,'2013-02-08 06:12:07','2013-02-08 06:12:07');
INSERT INTO "recipes_ingredient" VALUES(165,'Pastry',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(166,'Pecan nuts',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(167,'Prawns',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(168,'Prosciutto',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(169,'Red bell pepper',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(170,'Red onion',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(171,'Red wine',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(172,'Ricotta cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(173,'Roma tomatoes',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(174,'Rosemary',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(175,'Rumpsteak',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(176,'Sage leaves',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(177,'Salad spice',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(178,'Salmon',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(179,'Sausages',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(180,'Scallions',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(181,'Sesame oil',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(182,'Shrimps',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(183,'Smoked salmon',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(184,'Soy milk',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(185,'Spaghetti',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(186,'Spring onions',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(187,'Vinegar',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(188,'Watermelon',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(189,'White wine',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(190,'Duck fat',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(191,'Umeboshi',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_ingredient" VALUES(192,'Fresh laurel',50,'2013-02-08 06:23:50','2013-02-08 06:23:50');
INSERT INTO "recipes_ingredient" VALUES(193,'Paprika powder',50,'2013-02-08 06:25:47','2013-02-08 06:25:47');
INSERT INTO "recipes_ingredient" VALUES(194,'Fresh rosemary',50,'2013-02-08 06:26:43','2013-02-08 06:26:43');
INSERT INTO "recipes_tag" VALUES(1,'Main',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(2,'Side dish',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(3,'Salad',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(4,'Dessert',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(5,'Dip',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(6,'Cake',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(7,'Shake',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(8,'Appetizer',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(9,'Light',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(10,'Heavy',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(11,'Meat',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(12,'Fish',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(13,'Vegetarian',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(14,'Cheese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(15,'Pasta',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(16,'Rice',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(17,'French',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(18,'Italian',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(19,'Russian',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(20,'Hungarian',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(21,'Thai',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(22,'Ethnic',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(23,'Fusion',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(24,'Japanese',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(25,'Lunch',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(26,'Dinner',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(27,'Breakfast',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_tag" VALUES(29,'Sausage',50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipe" VALUES(1,'Locomoco','','Avocado, Cottage cheese, Eggs, Lettuce, Minced meat, Onion, Rice, Tomato','',50,'','2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipe" VALUES(2,'Cauliflower with chickpeas in tomato sauce','','Cauliflower, Chickpeas, Garlic, Paprika powder, Pesto, Rice, Tomato sauce, Zucchini','',50,'','2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(1,1,1,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(2,1,6,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(3,1,59,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(4,1,12,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(5,1,15,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(6,1,27,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(7,1,60,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(8,1,10,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(9,2,50,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(10,2,114,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(11,2,92,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(12,2,77,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(13,2,193,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(14,2,13,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(15,2,10,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipeingredient" VALUES(16,2,37,0.0,NULL,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipetag" VALUES(1,1,1,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipetag" VALUES(2,1,11,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipetag" VALUES(3,2,1,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
INSERT INTO "recipes_recipetag" VALUES(4,2,13,50,'2013-02-08 06:12:07.855000','2013-02-08 06:12:07.855000');
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
