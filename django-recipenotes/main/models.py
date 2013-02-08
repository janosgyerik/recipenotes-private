from datetime import datetime

from django.db import models


class Recipe(models.Model):
    _id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=80)
    summary = models.TextField(blank=True)
    display_name = models.TextField(blank=True)
    display_image = models.CharField(blank=True, max_length=80)
    display_order = models.IntegerField(default=50)
    memo = models.TextField(blank=True)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    def __unicode__(self):
        return self.name


class Ingredient(models.Model):
    _id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=80, unique=True)
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    def __unicode__(self):
        return self.name

    class Meta:
        ordering = ('name',)


class Unit(models.Model):
    _id = models.AutoField(primary_key=True)
    short_name = models.CharField(max_length=10, unique=True)
    long_name = models.CharField(max_length=20, unique=True)
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class RecipeIngredient(models.Model):
    _id = models.AutoField(primary_key=True)
    recipe = models.ForeignKey(Recipe)
    ingredient = models.ForeignKey(Ingredient)
    amount = models.FloatField(blank=True)
    unit = models.ForeignKey(Unit, null=True)
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    class Meta:
        unique_together = (('recipe', 'ingredient',))
        ordering = ('recipe', 'ingredient')


class Tag(models.Model):
    _id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=40, unique=True)
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    def __unicode__(self):
        return self.name

    class Meta:
        ordering = ('name',)


class RecipeTag(models.Model):
    _id = models.AutoField(primary_key=True)
    recipe = models.ForeignKey(Recipe)
    tag = models.ForeignKey(Tag)
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    class Meta:
        unique_together = (('recipe', 'tag',))
        ordering = ('recipe', 'tag')


class RecipeStep(models.Model):
    _id = models.AutoField(primary_key=True)
    recipe = models.ForeignKey(Recipe)
    step = models.TextField()
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    class Meta:
        unique_together = (('recipe', 'step',))


class RecipePhoto(models.Model):
    _id = models.AutoField(primary_key=True)
    recipe = models.ForeignKey(Recipe)
    filename = models.CharField(max_length=50)
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)

    class Meta:
        unique_together = (('recipe', 'filename',))


class FavoriteRecipe(models.Model):
    _id = models.AutoField(primary_key=True)
    recipe = models.ForeignKey(Recipe, unique=True)
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class FavoriteIngredient(models.Model):
    _id = models.AutoField(primary_key=True)
    ingredient = models.ForeignKey(Ingredient, unique=True)
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class FavoriteRecipePhoto(models.Model):
    _id = models.AutoField(primary_key=True)
    recipe_photo = models.ForeignKey(RecipePhoto, unique=True)
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class FavoriteTag(models.Model):
    _id = models.AutoField(primary_key=True)
    tag = models.ForeignKey(Tag, unique=True)
    display_order = models.IntegerField(default=50)
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


# eof
