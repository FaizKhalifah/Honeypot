module.exports = (sequelize, DataTypes) => {
    const Partner = sequelize.define("Partner", {
        partner_id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true,
        },
        name: DataTypes.STRING,
        address: DataTypes.TEXT,
    });

    Partner.associate = (models) => {
        Partner.hasMany(models.PartnerStock, {
            foreignKey: "partner_id",
            as: "PartnerStocks",
        });
        Partner.hasMany(models.Sale, {
            foreignKey: "partner_id",
            as: "Sales",
        });
    };

    return Partner;
};
