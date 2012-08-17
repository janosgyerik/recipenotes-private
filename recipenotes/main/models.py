from datetime import datetime

from django.db import models


class Recipe(models.Model):
    name = models.CharField(max_length=80)
    summary = models.TextField()
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class Ingredient(models.Model):
    name = models.CharField(max_length=80)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class Unit(models.Model):
    short_name = models.CharField(max_length=10)
    long_name = models.CharField(max_length=20)
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


class Tag(models.Model):
    name = models.CharField(max_length=40)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class RecipeTag(models.Model):
    recipe = models.ForeignKey(Recipe)
    tag = models.ForeignKey(Tag)
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


class RecipeStep(models.Model):
    recipe = models.ForeignKey(Recipe)
    step = models.TextField()
    display_order = models.IntegerField()
    created_dt = models.DateTimeField(default=datetime.now)
    updated_dt = models.DateTimeField(default=datetime.now)


# eof
