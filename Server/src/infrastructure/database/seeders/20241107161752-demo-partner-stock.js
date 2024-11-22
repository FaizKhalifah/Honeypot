"use strict";

module.exports = {
    up: async (queryInterface, Sequelize) => {
        return queryInterface.bulkInsert("PartnerStocks", [
            {
                partner_id: 1,
                product_id: 1,
                stock: 20,
                createdAt: new Date(),
                updatedAt: new Date(),
            },
            {
                partner_id: 1,
                product_id: 2,
                stock: 50,
                createdAt: new Date(),
                updatedAt: new Date(),
            },
            {
                partner_id: 2,
                product_id: 1,
                stock: 10,
                createdAt: new Date(),
                updatedAt: new Date(),
            },
        ]);
    },

    down: async (queryInterface, Sequelize) => {
        return queryInterface.bulkDelete("PartnerStocks", null, {});
    },
};
