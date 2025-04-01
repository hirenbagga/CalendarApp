package com.hask.hasktask.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/** import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;*/

@OpenAPIDefinition(
        info = @Info(

                version = "1.0",
                title = "Hask Task API Specs - Hiren Bagga, Anthony Stephen Aryee, Jarvan Dan",
                description = "Open API Documentation for Hask API",
                termsOfService = "By using this API, you 'AGREE & ACCEPT' our CS7319-Project Terms of Usage",

                contact = @Contact(
                        name = """
                                Hiren Bagga,\s
                                Anthony Stephen Aryee,\s
                                Jarvan Dan""",
                        email = "ID: 1001820, 1001821, 1001822",
                        url = "hask.com"
                ),

                license = @License(
                        name = "Developers API License",
                        url = "hask.com/license"
                )
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
        /* , servers = {
                @Server(
                        description = "Local Environment - Dev Mode",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production Environment - AWS Mode: IP",
                        url = "54.144.244.111" // replace with AWS instance
                ),
                @Server(
                        description = "Production Environment - AWS Mode: DNS",
                        url = "ec2-54-144-244-111.compute-1.amazonaws.com" // replace with AWS instance
                )
        },*/
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authorization Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig { }
