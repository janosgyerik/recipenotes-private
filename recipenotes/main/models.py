from datetime import datetime

from django.db import models


class Recipe(models.Model):
    name = models.CharField(max_length=80)
    summary = models.TextField()
    display_name = models.TextField()
    display_image = models.CharField(max_length=80)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class Ingredient(models.Model):
    name = models.CharField(max_length=80, unique=True)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class Unit(models.Model):
    short_name = models.CharField(max_length=10, unique=True)
    long_name = models.CharField(max_length=20, unique=True)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class RecipeIngredient(models.Model):
    recipe = models.ForeignKey(Recipe)
    ingredient = models.ForeignKey(Ingredient)
    amount = models.FloatField()
    unit = models.ForeignKey(Unit)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    class Meta:
        unique_together = (('recipe', 'ingredient',))


class Tag(models.Model):
    name = models.CharField(max_length=40, unique=True)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class RecipeTag(models.Model):
    recipe = models.ForeignKey(Recipe)
    tag = models.ForeignKey(Tag)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    class Meta:
        unique_together = (('recipe', 'tag',))


class RecipeStep(models.Model):
    recipe = models.ForeignKey(Recipe)
    step = models.TextField()
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    class Meta:
        unique_together = (('recipe', 'step',))


class RecipePhoto(models.Model):
    recipe = models.ForeignKey(Recipe)
    filename = models.CharField(max_length=50)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    class Meta:
        unique_together = (('recipe', 'filename',))


class FavoriteRecipe(models.Model):
    recipe = models.ForeignKey(Recipe, unique=True)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class FavoriteIngredient(models.Model):
    ingredient = models.ForeignKey(Ingredient, unique=True)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class FavoriteRecipePhoto(models.Model):
    recipe_photo = models.ForeignKey(RecipePhoto, unique=True)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class FavoriteTag(models.Model):
    tag = models.ForeignKey(Tag, unique=True)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


# eof
