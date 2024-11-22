module.exports = (sequelize, DataTypes) => {
    const Sale = sequelize.define("Sale", {
        sale_id: {
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
        sale_date: DataTypes.DATE,
        quantity: DataTypes.INTEGER,
        total_price: DataTypes.FLOAT,
    });

    Sale.associate = (models) => {
        Sale.belongsTo(models.Product, {
            foreignKey: "product_id",
            as: "Product", 
        });
        Sale.belongsTo(models.Partner, {
            foreignKey: "partner_id",
            as: "Partner", 
        });
    };

    return Sale;
};
