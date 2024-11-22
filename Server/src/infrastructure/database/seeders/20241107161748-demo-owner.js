"use strict";

const bcrypt = require("bcryptjs");

module.exports = {
    up: async (queryInterface, Sequelize) => {
        const hashedPassword = await bcrypt.hash("password123", 10);
        return queryInterface.bulkInsert("Owners", [
            {
                username: "owner1",
                password: hashedPassword,
                full_name: "Owner One",
                contact: "123456789",
                createdAt: new Date(),
                updatedAt: new Date(),
            },
            {
                username: "owner2",
                password: hashedPassword,
                full_name: "Owner Two",
                contact: "987654321",
                createdAt: new Date(),
                updatedAt: new Date(),
            },
        ]);
    },

    down: async (queryInterface, Sequelize) => {
        return queryInterface.bulkDelete("Owners", null, {});
    },
};
