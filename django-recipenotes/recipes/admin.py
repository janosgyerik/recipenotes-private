from django.contrib import admin

from recipes.models import Recipe
from recipes.models import Ingredient
from recipes.models import RecipeIngredient
from recipes.models import Tag
from recipes.models import RecipeTag

class RecipeIngredientAdmin(admin.ModelAdmin):
    list_display = ('recipe', 'ingredient')
    list_filter = ('recipe', 'ingredient')


class RecipeTagAdmin(admin.ModelAdmin):
    list_display = ('recipe', 'tag')
    list_filter = ('recipe', 'tag')


admin.site.register(Recipe)
admin.site.register(Ingredient)
admin.site.register(RecipeIngredient, RecipeIngredientAdmin)
admin.site.register(Tag)
admin.site.register(RecipeTag, RecipeTagAdmin)


# eof
