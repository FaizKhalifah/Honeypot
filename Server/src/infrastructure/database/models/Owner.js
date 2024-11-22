module.exports = (sequelize, DataTypes) => {
    const Owner = sequelize.define("Owner", {
        owner_id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true,
        },
        username: DataTypes.STRING,
        password: DataTypes.STRING,
        full_name: DataTypes.STRING,
        contact: DataTypes.STRING,
    });

    return Owner;
};
