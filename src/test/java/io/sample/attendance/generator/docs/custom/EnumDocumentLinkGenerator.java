package io.sample.attendance.generator.docs.custom;

public class EnumDocumentLinkGenerator {
    public static final String ENUM_LINK_FORMAT = "link:enums/%s.html[%s 코드, role=\"popup\"]";

    public enum TargetEnum {
        EXTRA_WORK_TYPE("extra-work-type", "추가 근무 타입");

        private final String htmlFileName;
        private final String linkName;

        TargetEnum(String htmlFileName, String linkName) {
            this.htmlFileName = htmlFileName;
            this.linkName = linkName;
        }
    }

    public static String enumLinkGenerator(TargetEnum targetEnum) {
        return String.format(ENUM_LINK_FORMAT, targetEnum.htmlFileName, targetEnum.linkName);
    }
}
