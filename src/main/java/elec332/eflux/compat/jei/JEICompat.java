package elec332.eflux.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 14-2-2016.
 */
public class JEICompat implements IModPlugin {
    /**
     * Called when the IJeiHelpers is available.
     * IModPlugins should store IJeiHelpers here if they need it.
     */
    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
    }

    /**
     * Called when the IItemRegistry is available, before register.
     */
    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {
    }

    /**
     * Register this mod plugin with the mod registry.
     * Called just before the game launches.
     * Will be called again if config
     */
    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCategories(new IRecipeCategory() {
            @Nonnull
            @Override
            public String getUid() {
                return null;
            }

            @Nonnull
            @Override
            public String getTitle() {
                return null;
            }

            @Nonnull
            @Override
            public IDrawable getBackground() {
                return null;
            }

            @Override
            public void drawExtras(Minecraft minecraft) {

            }

            @Override
            public void drawAnimations(Minecraft minecraft) {

            }

            @Override
            public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {

            }
        });
    }

    /**
     * Called when the IRecipeRegistry is available, after all mods have registered.
     */
    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {
        //recipeRegistry.addRecipe();
    }

}
