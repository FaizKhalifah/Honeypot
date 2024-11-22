"use strict";

module.exports = {
    up: async (queryInterface, Sequelize) => {
        await queryInterface.createTable("Sales", {
            sale_id: {
                type: Sequelize.INTEGER,
                autoIncrement: true,
                primaryKey: true,
            },
            product_id: {
                type: Sequelize.INTEGER,
                references: {
                    model: "Products", // Ensure this matches the table name
                    key: "product_id",
                },
                onUpdate: "CASCADE",
                onDelete: "CASCADE",
            },
            partner_id: {
                type: Sequelize.INTEGER,
                references: {
                    model: "Partners", // Ensure this matches the table name
                    key: "partner_id",
                },
                onUpdate: "CASCADE",
                onDelete: "CASCADE",
            },
            sale_date: {
                type: Sequelize.DATE,
                allowNull: false,
            },
            quantity: {
                type: Sequelize.INTEGER,
                allowNull: false,
            },
            total_price: {
                type: Sequelize.FLOAT,
                allowNull: false,
            },
            createdAt: {
                type: Sequelize.DATE,
                allowNull: false,
                defaultValue: Sequelize.literal("CURRENT_TIMESTAMP"),
            },
            updatedAt: {
                type: Sequelize.DATE,
                allowNull: false,
                defaultValue: Sequelize.literal("CURRENT_TIMESTAMP"),
            },
        });
    },

    down: async (queryInterface, Sequelize) => {
        await queryInterface.dropTable("Sales");
    },
};
