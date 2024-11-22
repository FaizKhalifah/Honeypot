module.exports = (sequelize, DataTypes) => {
    const PartnerStock = sequelize.define("PartnerStock", {
        partner_stock_id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true,
        },
        product_id: {
            type: DataTypes.INTEGER,
            references: { model: "Product", key: "product_id" },
        },
        partner_id: {
            type: DataTypes.INTEGER,
            references: { model: "Partner", key: "partner_id" },
        },
        stock: DataTypes.INTEGER,
    });

    PartnerStock.associate = (models) => {
        PartnerStock.belongsTo(models.Partner, {
            foreignKey: "partner_id",
            as: "Partner",
        });
        PartnerStock.belongsTo(models.Product, {
            foreignKey: "product_id",
            as: "Product",
        });
    };

    return PartnerStock;
};
