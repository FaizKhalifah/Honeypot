"use strict";

module.exports = {
    up: async (queryInterface, Sequelize) => {
        return queryInterface.bulkInsert("Partners", [
            {
                name: "Toko Oleh-Oleh 1",
                address: "Jl. Merdeka No. 1, Jakarta",
                createdAt: new Date(),
                updatedAt: new Date(),
            },
            {
                name: "Toko Oleh-Oleh 2",
                address: "Jl. Kemerdekaan No. 5, Bandung",
                createdAt: new Date(),
                updatedAt: new Date(),
            },
        ]);
    },

    down: async (queryInterface, Sequelize) => {
        return queryInterface.bulkDelete("Partners", null, {});
    },
};
