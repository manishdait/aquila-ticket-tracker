package io.github.manishdait.aquila.project;

import java.util.List;

public record ProjectRequest (String name, String code, String description, List<String> teamMembers) {
    
}
