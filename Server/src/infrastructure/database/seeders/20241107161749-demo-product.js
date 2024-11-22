"use strict";

module.exports = {
    up: async (queryInterface, Sequelize) => {
        return queryInterface.bulkInsert("Products", [
            {
                name: "Gula Aren",
                description: "Organic sugar made from palm sap.",
                stock: 100,
                price_per_unit: 50000,
                createdAt: new Date(),
                updatedAt: new Date(),
            },
            {
                name: "Gula Tebu",
                description: "Refined sugar made from sugarcane.",
                stock: 200,
                price_per_unit: 10000,
                createdAt: new Date(),
                updatedAt: new Date(),
            },
        ]);
    },

    down: async (queryInterface, Sequelize) => {
        return queryInterface.bulkDelete("Products", null, {});
    },
};
