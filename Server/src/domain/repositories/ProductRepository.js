class ProductRepository {
    constructor(productModel) {
        this.productModel = productModel;
    }

    async createProduct(productData) {
        return await this.productModel.create(productData);
    }

    async updateProduct(productId, updatedData) {
        return await this.productModel.update(updatedData, {
            where: { product_id: productId },
        });
    }

    async updateStock(productId, newStock) {
        return await this.productModel.update(
            { stock: newStock },
            { where: { product_id: productId } }
        );
    }

    async findAllProducts() {
        return await this.productModel.findAll();
    }

    async findProductById(productId) {
        return await this.productModel.findByPk(productId);
    }

    async deleteProduct(productId) {
        return await this.productModel.destroy({
            where: { product_id: productId },
        });
    }
}

module.exports = ProductRepository;
