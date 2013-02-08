from django.contrib import admin

from main.models import Recipe
from main.models import Ingredient
from main.models import RecipeIngredient
from main.models import Tag
from main.models import RecipeTag

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
