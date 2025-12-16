package Model.Permission;

import java.util.List;

import Model.Bean.UserPermission;

public class PermissionBO {

    private PermissionDAO permissionDAO = new PermissionDAO();

    public PermissionDAO getPermissionDAO() {
        return permissionDAO;
    }

    /** Lấy danh sách fid theo usid */
    public List<Integer> getListFidByUsid(int usid) {
        if (usid <= 0)
            return null;
        return permissionDAO.getListFidByUsid(usid);
    }

    /** Lưu quyền cho user */
    public boolean saveUserPermission(int usid, int fid, String accessMode) {
        if (accessMode == null)
            accessMode = "";
        if (usid <= 0 || fid <= 0 || accessMode == null || accessMode.isEmpty())
            return false;

        // Xóa quyền cũ nếu đã tồn tại
        permissionDAO.deleteUserPermission(usid, fid);

        // Thêm quyền mới
        return permissionDAO.insertUserPermission(usid, fid, accessMode);
    }

    /** Thêm các method cho AdminController */
    public boolean deleteUserPermission(int usid, int fid) {
        if (usid <= 0 || fid <= 0)
            return false;
        return permissionDAO.deleteUserPermission(usid, fid);
    }

    public boolean insertUserPermission(int usid, int fid, String accessMode) {
        if (accessMode == null)
            accessMode = "";
        if (usid <= 0 || fid <= 0 || accessMode == null || accessMode.isEmpty())
            return false;
        return permissionDAO.insertUserPermission(usid, fid, accessMode);
    }

    public List<UserPermission> getUserPermissionsByUsid(int usid) {
        return permissionDAO.getUserPermissionsByUsid(usid);
    }

    public String getAccessMode(int usid, int fid) {
        if (usid <= 0 || fid <= 0)
            return null;
        return permissionDAO.getAccessMode(usid, fid);
    }
}
