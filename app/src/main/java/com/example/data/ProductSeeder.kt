package com.example.data

object ProductSeeder {
    fun getPreseededProducts(): List<HairProduct> {
        return listOf(
            // SHAMPOOS
            HairProduct(
                id = "shampoo_antifungal",
                name = "Scalp Pure Zinc-Keto Cleanse",
                brand = "Trichology Labs",
                category = "Shampoo",
                targetProblem = "Dandruff",
                ingredients = "Ketoconazole 1%, Zinc Pyrithione 1%, Tea Tree Oil, Salicylic Acid (BHA)",
                hairTypeCompatibility = "All Hair Types",
                price = 24.99,
                reasonForSelection = "Formulated with therapeutic antifungals to eradicate dandruff-causing Malassezia yeast and soothe itchy scalp irritation.",
                usageInstructions = "Lather into wet hair, massage gently into scalp for 2-3 minutes. Let sit before rinsing thoroughly.",
                frequencyPerWeek = 3,
                imageUrlPlaceholder = "Blue"
            ),
            HairProduct(
                id = "shampoo_moisture",
                name = "Hydra Moisture Silk Shampoo",
                brand = "Aura Botanicals",
                category = "Shampoo",
                targetProblem = "Dryness",
                ingredients = "Argan Oil, Shea Butter, Aloe Vera, Hyaluronic Acid, Squalane",
                hairTypeCompatibility = "Dry, Curly, Coily",
                price = 19.99,
                reasonForSelection = "Sulfate-free hydrating cleanser that gently removes impurities without stripping the hair's natural sebum barrier.",
                usageInstructions = "Apply to wet hair, focus massage on the roots, then run through mid-lengths and rinse.",
                frequencyPerWeek = 2,
                imageUrlPlaceholder = "Green"
            ),
            HairProduct(
                id = "shampoo_active_caffeine",
                name = "Follicle Defense Caffeine Infused Cleanser",
                brand = "Root Dynamics",
                category = "Shampoo",
                targetProblem = "Hair Fall",
                ingredients = "Anhydrous Caffeine, Biotin, Hydrolyzed Keratin, Niacinamide, Saw Palmetto Extract",
                hairTypeCompatibility = "Straight, Wavy, Thinning",
                price = 27.50,
                reasonForSelection = "Stimulates scalp micro-circulation and targets DHT build-up to help reduce follicle miniaturization and styling breakage.",
                usageInstructions = "Work into a rich lather. Massage deeply code-by-code across the scalp. Leave on for 3 minutes before rinsing.",
                frequencyPerWeek = 4,
                imageUrlPlaceholder = "Amber"
            ),
            HairProduct(
                id = "shampoo_sensitive",
                name = "Calm Relief Fragrance-Free Cleanser",
                brand = "DermoSoothe",
                category = "Shampoo",
                targetProblem = "Irritation",
                ingredients = "Colloidal Oatmeal, Chamomile Extract, Panthenol (Pro-Vitamin B5), Licorice Root",
                hairTypeCompatibility = "All Hair Types, Sensitive Scalp",
                price = 21.50,
                reasonForSelection = "Hypoallergenic cleanser free of sulfates, parabens, and essential oil allergens, specifically tested to reduce burning or redness.",
                usageInstructions = "Gently massage into scalp. Avoid rubbing raw areas intensely. Rinse with lukewarm water.",
                frequencyPerWeek = 3,
                imageUrlPlaceholder = "Teal"
            ),

            // CONDITIONERS
            HairProduct(
                id = "conditioner_keratin",
                name = "Keratin Repair Fortifying Conditioner",
                brand = "Root Dynamics",
                category = "Conditioner",
                targetProblem = "Hair Fall",
                ingredients = "Keratin Amino Acids, Hydrolyzed Wheat Protein, Sweet Almond Oil, Arginine",
                hairTypeCompatibility = "Damaged, Weak Hair",
                price = 22.00,
                reasonForSelection = "Replenishes porous structural gaps in the hair cuticle, increasing elasticity and reducing tensile hair breakage.",
                usageInstructions = "After shampooing, apply generously to mid-lengths and ends. Leave in for 3-5 minutes, then rinse with cool water.",
                frequencyPerWeek = 3,
                imageUrlPlaceholder = "Amber"
            ),
            HairProduct(
                id = "conditioner_moisture",
                name = "Deep Moisture Rich Conditioner",
                brand = "Aura Botanicals",
                category = "Conditioner",
                targetProblem = "Dryness",
                ingredients = "Macadamia Nut Oil, Jojoba Esters, Glycerin, Meadowfoam Seed Oil",
                hairTypeCompatibility = "Dry, Frizzy, Wavy, Curly",
                price = 18.50,
                reasonForSelection = "Creates a moisture-locking seal that smooths cuticle scales, blocks environmental frizz, and reveals brilliant shine.",
                usageInstructions = "Distribute evenly from mid-shaft to ends. Detangle gently with a wide-tooth comb. Rinse after 3 minutes.",
                frequencyPerWeek = 2,
                imageUrlPlaceholder = "Green"
            ),

            // HAIR OILS
            HairProduct(
                id = "hair_oil_rosemary",
                name = "Rosemary Rosemary Follicle Complex",
                brand = "Trichology Labs",
                category = "Hair Oil",
                targetProblem = "Hair Fall",
                ingredients = "Rosemary Leaf Oil, Castor Oil, Pumpkin Seed Oil, Peppermint Essential Oil",
                hairTypeCompatibility = "Thinning Scalps",
                price = 29.99,
                reasonForSelection = "Rosemary oil has clinically studied hair-regrowth benefits comparable to minoxidil 2%, nourishing blood vessels to jumpstart follicle activity.",
                usageInstructions = "Apply 4-5 drops directly to clean scalp. Massage using fingertips for 10 minutes. Can be washed out after 2 hours or left overnight.",
                frequencyPerWeek = 3,
                imageUrlPlaceholder = "Amber"
            ),
            HairProduct(
                id = "hair_oil_argan",
                name = "Premium Cold-Pressed Moroccan Argan Oil",
                brand = "Aura Botanicals",
                category = "Hair Oil",
                targetProblem = "Dryness",
                ingredients = "100% Pure Organic Argania Spinosa Kernel Oil, Vitamin E",
                hairTypeCompatibility = "Curly, Dried, Textured",
                price = 25.00,
                reasonForSelection = "Rich in Oleic and Linoleic fatty acids, this lightweight oil penetrates the hair cortex to restore internal flexibility and brilliant glow.",
                usageInstructions = "Dispense 2-3 drops into palms. Rub together and smooth through damp hair before styling or apply to dry ends.",
                frequencyPerWeek = 7,
                imageUrlPlaceholder = "Green"
            ),

            // SERUMS
            HairProduct(
                id = "serum_scalp_dandruff",
                name = "Salicylic Acid Soothing Scalp Serum",
                brand = "Trichology Labs",
                category = "Serum",
                targetProblem = "Dandruff",
                ingredients = "Salicylic Acid 2%, Zinc PCA, Celery Seed Extract, Centella Asiatica",
                hairTypeCompatibility = "Flaky/Itchy Scalp",
                price = 28.00,
                reasonForSelection = "Gently exfoliates dead skin scale buildup, decreases excess sebum production, and relieves persistent scalp itchiness immediately.",
                usageInstructions = "Locally part dry hair and apply several drops directly across target zones. Rub in. Do not rinse out.",
                frequencyPerWeek = 4,
                imageUrlPlaceholder = "Blue"
            ),
            HairProduct(
                id = "serum_frizz_shield",
                name = "Sleek Guard Anti-Frizz Gloss Serum",
                brand = "DermoSoothe",
                category = "Serum",
                targetProblem = "Frizz",
                ingredients = "Meadowfoam Estolide, Camellia seed Oil, Cyclopentasiloxane, Squalane",
                hairTypeCompatibility = "Frizzy, Porous Hair",
                price = 22.99,
                reasonForSelection = "Forms a feather-light hydrophobic envelope around the hair shaft, repelling atmospheric humidity of up to 90%.",
                usageInstructions = "Rub a dime-sized amount between fingers and brush hands through damp hair, focusing on outer flyaway layers.",
                frequencyPerWeek = 5,
                imageUrlPlaceholder = "Teal"
            )
        )
    }
}
