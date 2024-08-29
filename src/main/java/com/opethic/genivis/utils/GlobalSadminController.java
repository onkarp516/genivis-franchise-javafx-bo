package com.opethic.genivis.utils;


import com.opethic.genivis.controller.users.CadminController;
import com.opethic.genivis.controller.users.SadminController;
import com.opethic.genivis.dto.FranchiseDTO;


public final class GlobalSadminController {
    private SadminController controller;

    private final static GlobalSadminController INSTANCE = new GlobalSadminController();

    private GlobalSadminController() {
    }

    public static GlobalSadminController getInstance() {
        return INSTANCE;
    }

    public void setController(SadminController controller) {
        this.controller = controller;
    }

    public void addTabStatic(String slug, Boolean fl) {
//        System.out.println("GlobalController slug" + slug);
        try {
            if (controller != null) {
                controller.GCSlug(slug, fl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addTabStatic(String slug, Boolean fl, FranchiseDTO franchiseDTO) {
//        System.out.println("GlobalController slug" + slug);
        try {
            if (controller != null) {
                controller.GCSlugWithFrParam(slug, fl,franchiseDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTabStaticWithParam(String slug, Boolean fl, Integer id) {
//        System.out.println("GlobalController slug" + slug);
        try {
            if (controller != null) {
                controller.GCSlugWithParam(slug, fl, id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // new method - reports id passing
    public void addTabStatic1(String slug, Boolean fl, Integer id) {
//        System.out.println("GlobalController slug" + slug);
//        System.out.println("GlobalController id" + id);
        try {
            if (controller != null) {
                controller.GCSlug1(slug, fl, id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
