const { Sequelize } = require("sequelize");

class SaleRepository {
    constructor(saleModel, partnerModel, productModel) {
        this.saleModel = saleModel;
        this.partnerModel = partnerModel;
        this.productModel = productModel;
    }

    async findSalesByMonth(partnerId, month, year) {
        return await this.saleModel.findAll({
            where: {
                partner_id: partnerId,
                sale_date: {
                    [Sequelize.Op.between]: [
                        new Date(year, month - 1, 1),
                        new Date(year, month, 0, 23, 59, 59),
                    ],
                },
            },
            include: [{ model: this.productModel, as: "Product" }],
        });
    }

    async createSale(saleData) {
        return await this.saleModel.create(saleData);
    }

    async getPartnerStock(partnerId, productId) {
        return await this.partnerStockModel.findOne({
            where: { partner_id: partnerId, product_id: productId },
            include: [{ model: this.productModel, as: "Product" }], // Ensure Product is included
        });
    }

    async updatePartnerStock(partnerId, productId, newStock) {
        return await this.partnerStockModel.update(
            { stock: newStock },
            { where: { partner_id: partnerId, product_id: productId } }
        );
    }

    async getTotalSalesData() {
        // Aggregate sales by partner and product
        const salesData = await this.saleModel.findAll({
            attributes: [
                [Sequelize.col("Sale.partner_id"), "partner_id"],
                [Sequelize.col("Sale.product_id"), "product_id"],
                [
                    Sequelize.fn("SUM", Sequelize.col("Sale.quantity")),
                    "total_quantity",
                ],
                [
                    Sequelize.fn("SUM", Sequelize.col("Sale.total_price")),
                    "total_revenue",
                ],
            ],
            include: [
                {
                    model: this.partnerModel,
                    as: "Partner",
                    attributes: ["partner_id", "name"],
                },
                {
                    model: this.productModel,
                    as: "Product",
                    attributes: ["product_id", "name", "price_per_unit"],
                },
            ],
            group: [
                Sequelize.col("Sale.partner_id"),
                Sequelize.col("Sale.product_id"),
                Sequelize.col("Partner.partner_id"),
                Sequelize.col("Partner.name"),
                Sequelize.col("Product.product_id"),
                Sequelize.col("Product.name"),
                Sequelize.col("Product.price_per_unit"),
            ],
        });

        // Aggregate overall total sales
        const overallTotal = await this.saleModel.findOne({
            attributes: [
                [
                    Sequelize.fn("SUM", Sequelize.col("quantity")),
                    "total_quantity",
                ],
                [
                    Sequelize.fn("SUM", Sequelize.col("total_price")),
                    "total_revenue",
                ],
            ],
        });

        // Convert total_quantity to integers in salesData and overallTotal
        salesData.forEach((sale) => {
            sale.dataValues.total_quantity = parseInt(
                sale.dataValues.total_quantity,
                10
            );
            sale.dataValues.total_revenue = parseFloat(
                sale.dataValues.total_revenue
            );
        });

        overallTotal.dataValues.total_quantity = parseInt(
            overallTotal.dataValues.total_quantity,
            10
        );
        overallTotal.dataValues.total_revenue = parseFloat(
            overallTotal.dataValues.total_revenue
        );

        return { salesData, overallTotal };
    }
}

module.exports = SaleRepository;
