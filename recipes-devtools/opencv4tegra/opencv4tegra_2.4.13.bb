DESCRIPTION = "NVIDIA OpenCV4Tegra for L4T 21.x Packages"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://usr/share/doc/libopencv4tegra-repo/copyright;md5=99d8c0c1313afdf990f6407c07a88407"

SRC_URI = "http://developer.download.nvidia.com/devzone/devcenter/mobile/jetpack_l4t/003/linux-x64/libopencv4tegra-repo_2.4.13_armhf_l4t-r21.deb"
SRC_URI[md5sum] = "0db305a4d24a9211606bb1fb8ec2d480"
SRC_URI[sha256sum] = "9bfa9d5df0c3218f5b4000c8d9d50d136cd8e6ad82384b7781719deb7093ae21"

S = "${WORKDIR}"
B = "${WORKDIR}/build"

DEPENDS = "dpkg-native" 

COMPATIBLE_MACHINE = "(tegra124)"
PACKAGE_ARCH = "${SOC_FAMILY_PKGARCH}"

PACKAGES += "${PN}-apps"

DEPENDS = "tiff zlib jpeg glib-2.0 python gtk+ libpng libav jasper tbb"
RDEPENDS_${PN} = "cuda-toolkit cuda-cudart"
RDEPENDS_${PN}-dev += "${PN} ${PN}-staticdev"

INSANE_SKIP_${PN} = "ldflags"
INSANE_SKIP_${PN}-dev = "ldflags"

FILES_${PN}-apps += "${bindir}"
FILES_${PN} +=  "${libdir}/*"
FILES_${PN}-dev += "${includedir} ${datadir}/OpenCV/*"

do_compile () {
    cp -r ${WORKDIR}/usr ${D}
    dpkg-deb --extract ${WORKDIR}/var/libopencv4tegra-repo/libopencv4tegra_2.4.13_armhf.deb ${B}
    dpkg-deb --extract ${WORKDIR}/var/libopencv4tegra-repo/libopencv4tegra-dev_2.4.13_armhf.deb ${B}
    dpkg-deb --extract ${WORKDIR}/var/libopencv4tegra-repo/libopencv4tegra-python_2.4.13_armhf.deb ${B}

}

do_install () {
    install -d ${D}${prefix} ${D}${libdir} ${D}${datadir}/doc ${D}${includedir} ${D}${bindir}
    cp -R --preserve=mode,timestamps ${B}/usr/bin/* ${D}${bindir}
    cp -R --preserve=mode,timestamps ${B}/usr/include/* ${D}${includedir}
    cp -R --preserve=mode,timestamps ${B}/usr/lib/* ${D}${libdir}
    cp -R --preserve=mode,timestamps ${B}/usr/share/doc/* ${D}${datadir}/doc
    cp -R --preserve=mode,timestamps ${B}/usr/share/OpenCV ${D}${datadir}/OpenCV

    #Fix cmake files that have these includes, since it appears they break building any new project with that
    sed -i 's|opencv_dep_cudart|cudart|g' ${D}/${datadir}/OpenCV/OpenCVModules*
    sed -i 's|opencv_dep_cufft|cufft|g' ${D}/${datadir}/OpenCV/OpenCVModules*
    sed -i 's|opencv_dep_nppc|nppc|g' ${D}/${datadir}/OpenCV/OpenCVModules*
    sed -i 's|opencv_dep_nppi|nppi|g' ${D}/${datadir}/OpenCV/OpenCVModules*
    sed -i 's|opencv_dep_npps|npps|g' ${D}/${datadir}/OpenCV/OpenCVModules* 
}

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_SYSROOT_STRIP = "1"

