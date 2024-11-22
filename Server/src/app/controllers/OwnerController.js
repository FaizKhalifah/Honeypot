class OwnerController {
    constructor(ownerUseCase) {
        this.ownerUseCase = ownerUseCase;
    }

    async register(req, res) {
        try {
            const owner = await this.ownerUseCase.register(req.body);
            res.status(201).json(owner);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async login(req, res) {
        try {
            const { username, password } = req.body;
            const result = await this.ownerUseCase.login(username, password);
            res.status(200).json(result);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async getProfile(req, res) {
        try {
            const owner = await this.ownerUseCase.getProfile(req.ownerId);
            res.status(200).json(owner);
        } catch (error) {
            res.status(404).json({ message: error.message });
        }
    }

    async updateProfile(req, res) {
        try {
            const updatedOwner = await this.ownerUseCase.updateProfile(
                req.ownerId,
                req.body
            );
            res.status(200).json(updatedOwner);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async changePassword(req, res) {
        try {
            const { currentPassword, newPassword } = req.body;
            await this.ownerUseCase.changePassword(
                req.ownerId,
                currentPassword,
                newPassword
            );
            res.status(200).json({ message: "Password updated successfully" });
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async deleteProfile(req, res) {
        try {
            await this.ownerUseCase.deleteProfile(req.ownerId);
            res.status(200).json({ message: "Profile deleted successfully" });
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }
}

module.exports = OwnerController;
