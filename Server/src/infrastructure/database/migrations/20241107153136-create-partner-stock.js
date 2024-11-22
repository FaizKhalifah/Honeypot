"use strict";

module.exports = {
    up: async (queryInterface, Sequelize) => {
        await queryInterface.createTable("PartnerStocks", {
            partner_stock_id: {
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
            stock: {
                type: Sequelize.INTEGER,
                defaultValue: 0,
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
        await queryInterface.dropTable("PartnerStocks");
    },
};
