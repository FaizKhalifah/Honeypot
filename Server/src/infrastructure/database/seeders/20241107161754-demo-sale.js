"use strict";

module.exports = {
    up: async (queryInterface, Sequelize) => {
        return queryInterface.bulkInsert("Sales", [
            {
                partner_id: 1,
                product_id: 1,
                sale_date: new Date(),
                quantity: 5,
                total_price: 250000,
                createdAt: new Date(),
                updatedAt: new Date(),
            },
            {
                partner_id: 1,
                product_id: 2,
                sale_date: new Date(),
                quantity: 10,
                total_price: 100000,
                createdAt: new Date(),
                updatedAt: new Date(),
            },
            {
                partner_id: 2,
                product_id: 1,
                sale_date: new Date(),
                quantity: 3,
                total_price: 150000,
                createdAt: new Date(),
                updatedAt: new Date(),
            },
        ]);
    },

    down: async (queryInterface, Sequelize) => {
        return queryInterface.bulkDelete("Sales", null, {});
    },
};
