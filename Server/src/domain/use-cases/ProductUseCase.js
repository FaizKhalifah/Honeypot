class ProductUseCase {
    constructor(productRepository) {
        this.productRepository = productRepository;
    }

    async addProduct(productData) {
        return await this.productRepository.createProduct(productData);
    }

    async updateProduct(productId, updatedData) {
        return await this.productRepository.updateProduct(
            productId,
            updatedData
        );
    }

    async updateStock(productId, newStock) {
        return await this.productRepository.updateStock(productId, newStock);
    }

    async getAllProducts() {
        return await this.productRepository.findAllProducts();
    }

    async getProductDetails(productId) {
        return await this.productRepository.findProductById(productId);
    }

    async deleteProduct(productId) {
        return await this.productRepository.deleteProduct(productId);
    }
}

module.exports = ProductUseCase;
