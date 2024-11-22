class PartnerRepository {
    constructor(partnerModel, partnerStockModel, productModel) {
        this.partnerModel = partnerModel;
        this.partnerStockModel = partnerStockModel;
        this.productModel = productModel;
    }

    async findAllPartners() {
        return await this.partnerModel.findAll({
            include: {
                model: this.partnerStockModel,
                as: "PartnerStocks",
                include: [{ model: this.productModel, as: "Product" }],
            },
        });
    }

    async findPartnerById(partnerId) {
        return await this.partnerModel.findByPk(partnerId, {
            include: {
                model: this.partnerStockModel,
                as: "PartnerStocks",
                include: [{ model: this.productModel, as: "Product" }],
            },
        });
    }

    async updatePartner(partnerId, updatedData) {
        return await this.partnerModel.update(updatedData, {
            where: { partner_id: partnerId },
        });
    }

    async addOrUpdatePartnerStock(partnerId, productId, stockChange) {
        const product = await this.productModel.findByPk(productId);
        const partnerStock = await this.partnerStockModel.findOne({
            where: { partner_id: partnerId, product_id: productId },
        });

        if (partnerStock) {
            const newPartnerStock = partnerStock.stock + stockChange;
            if (newPartnerStock < 0)
                throw new Error("Insufficient stock in partner.");

            await this.partnerStockModel.update(
                { stock: newPartnerStock },
                { where: { partner_id: partnerId, product_id: productId } }
            );

            // Adjust the product's stock
            await this.productModel.update(
                { stock: product.stock - stockChange },
                { where: { product_id: productId } }
            );
        } else {
            // If partner stock entry doesn't exist, create it and adjust product stock
            await this.partnerStockModel.create({
                partner_id: partnerId,
                product_id: productId,
                stock: stockChange,
            });
            await this.productModel.update(
                { stock: product.stock - stockChange },
                { where: { product_id: productId } }
            );
        }
    }

    async deletePartner(partnerId) {
        const partnerStocks = await this.partnerStockModel.findAll({
            where: { partner_id: partnerId },
        });

        // Revert stock back to product table before deleting partner
        for (const stock of partnerStocks) {
            const product = await this.productModel.findByPk(stock.product_id);
            await this.productModel.update(
                { stock: product.stock + stock.stock },
                { where: { product_id: stock.product_id } }
            );
        }

        // Delete associated partner stock entries
        await this.partnerStockModel.destroy({
            where: { partner_id: partnerId },
        });

        // Delete the partner
        return await this.partnerModel.destroy({
            where: { partner_id: partnerId },
        });
    }
}

module.exports = PartnerRepository;
