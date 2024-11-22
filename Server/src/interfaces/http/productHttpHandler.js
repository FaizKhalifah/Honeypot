const ProductController = require("../../app/controllers/ProductController");
const ProductRepository = require("../../domain/repositories/ProductRepository");
const ProductUseCase = require("../../domain/use-cases/ProductUseCase");
const { Product } = require("../../infrastructure/database/models");

const productRepository = new ProductRepository(Product);
const productUseCase = new ProductUseCase(productRepository);
const productController = new ProductController(productUseCase);

const productHttpHandler = {
    async addProduct(req, res) {
        return productController.addProduct(req, res);
    },

    async updateProduct(req, res) {
        return productController.updateProduct(req, res);
    },

    async updateStock(req, res) {
        return productController.updateStock(req, res);
    },

    async getAllProducts(req, res) {
        return productController.getAllProducts(req, res);
    },

    async getProductDetails(req, res) {
        return productController.getProductDetails(req, res);
    },

    async deleteProduct(req, res) {
        return productController.deleteProduct(req, res);
    },
};

module.exports = productHttpHandler;
