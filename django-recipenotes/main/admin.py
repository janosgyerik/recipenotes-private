from django.contrib import admin

from main.models import Recipe
from main.models import Ingredient
from main.models import Tag
from main.models import RecipeIngredient
from main.models import RecipeTag


admin.site.register(Recipe)
admin.site.register(Ingredient)
admin.site.register(Tag)
admin.site.register(RecipeIngredient)
admin.site.register(RecipeTag)


# eof
