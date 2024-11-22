module.exports = (sequelize, DataTypes) => {
    const Product = sequelize.define("Product", {
        product_id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true,
        },
        name: DataTypes.STRING,
        description: DataTypes.TEXT,
        stock: DataTypes.INTEGER,
        price_per_unit: DataTypes.FLOAT,
    });

    Product.associate = (models) => {
        Product.hasMany(models.PartnerStock, {
            foreignKey: "product_id",
            as: "PartnerStocks",
        });
        Product.hasMany(models.Sale, {
            foreignKey: "product_id",
            as: "Sales",
        });
    };

    return Product;
};
