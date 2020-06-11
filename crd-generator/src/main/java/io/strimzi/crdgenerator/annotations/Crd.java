/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.crdgenerator.annotations;

import io.fabric8.kubernetes.api.model.extensions.Scale;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Some info from the top level of a {@code CustomResourceDefinition}.
 * @see <a href="https://v1-9.docs.kubernetes.io/docs/reference/generated/kubernetes-api/v1.9/#customresourcedefinition-v1beta1-apiextensions">API Reference</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Crd {

    /**
     * The {@code apiVersion} of the generated {@code CustomResourceDefinition}.
     * (Not the {@code apiVersion} of your custom resource instances, which is {@link Spec#version()}).
     * @return The {@code apiVersion} of the generated {@code CustomResourceDefinition}
     */
    String apiVersion();

    /**
     * Info for the {@code spec} of the generated {@code CustomResourceDefinition}.
     * @return The spec.
     */
    Spec spec();

    /**
     * Some info from the {@code spec} part of a {@code CustomResourceDefinition}.
     */
    @Target({})
    @interface Spec {

        /**
         * @return The API group for the custom resource instances.
         */
        String group();

        Names names();

        @Target({})
        @interface Names {
            /**
             * @return The kind of the resource
             */
            String kind();

            /**
             * @return The list kind. Defaults to ${{@linkplain #kind()}}List.
             */
            String listKind() default "";

            /**
             * @return The singular of the resource. Defaults to {@link #kind()}.
             */
            String singular() default "";

            /**
             * @return The plural of the resource.
             */
            String plural();

            /**
             * @return Short names (e.g. "svc" is the short name for the K8S "services" kind).
             */
            String[] shortNames() default {};

            /**
             * @return A list of grouped resources custom resources belong to.
             */
            String[] categories() default {};
        }

        /**
         * @return The scope of the resources. E.g. "Namespaced".
         */
        String scope();

        /**
         * @return The version of custom resources that this is the definition for.
         * @see <a href="https://v1-11.docs.kubernetes.io/docs/reference/generated/kubernetes-api/v1.11/#customresourcedefinitionversion-v1beta1-apiextensions">Kubernetes 1.11 API documtation</a>
         * @see #versions()
         */
        String version() default "";

        /**
         * @return The version of custom resources that this is the definition for.
         * @see <a href="https://v1-11.docs.kubernetes.io/docs/reference/generated/kubernetes-api/v1.11/#customresourcedefinitionversion-v1beta1-apiextensions">Kubernetes 1.11 API documtation</a>
         */
        Version[] versions() default {};

        /**
         * The version of custom resources that this is the definition for.
         * @see <a href="https://v1-11.docs.kubernetes.io/docs/reference/generated/kubernetes-api/v1.11/#customresourcedefinitionversion-v1beta1-apiextensions">Kubernetes 1.11 API documtation</a>
         */
        @interface Version {
            String name();
            boolean served();
            boolean storage();
        }

        /**
         * @return The subresources of a custom resources that this is the definition for.
         * @see <a href="https://v1-11.docs.kubernetes.io/docs/reference/generated/kubernetes-api/v1.11/#customresourcedefinitionversion-v1beta1-apiextensions">Kubernetes 1.11 API documtation</a>
         */
        Subresources subresources() default @Subresources(
                status = {},
                scale = {}
                );

        /**
         * The subresources of a custom resources that this is the definition for.
         * @see <a href="https://v1-11.docs.kubernetes.io/docs/reference/generated/kubernetes-api/v1.11/#customresourcedefinitionversion-v1beta1-apiextensions">Kubernetes 1.11 API documtation</a>
         */
        @interface Subresources {
            Status[] status();
            Scale[] scale() default {};

            @interface Status {
            }

            /**
             * The scale subresource of a custom resources that this is the definition for.
             * @see <a href="https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.18/#customresourcesubresourcescale-v1beta1-apiextensions-k8s-io">Kubernetes 1.18 API documtation</a>
             */
            @interface Scale {
                String specReplicasPath();
                String statusReplicasPath();
                String labelSelectorPath() default "";
            }
        }

        /**
         * @return Additional printer columns.
         * @see <a href="https://v1-11.docs.kubernetes.io/docs/reference/generated/kubernetes-api/v1.11/#customresourcecolumndefinition-v1beta1-apiextensions">Kubernetes 1.11 API documtation</a>
         */
        AdditionalPrinterColumn[] additionalPrinterColumns() default {};

        /**
         * Additional printer columns.
         * @see <a href="https://v1-11.docs.kubernetes.io/docs/reference/generated/kubernetes-api/v1.11/#customresourcecolumndefinition-v1beta1-apiextensions">Kubernetes 1.11 API documtation</a>
         */
        @interface AdditionalPrinterColumn {
            /** @return JSON path into the CR for the value to show */
            String jsonPath();
            /** @return The description of the column */
            String description();
            /**
             * One of:
             * int32
             * int64
             * float
             * double
             * byte
             * date
             * date-time
             * password
             * @return The format
             */
            String format() default "";
            /** @return The name of the column */
            String name();
            /** @return 0 to show in standard view, greater than zero to show only in wide view */
            int priority() default 0;
            /**
             * One of:
             * integer,
             * number,
             * string,
             * boolean,
             * date
             * @return The JSON type.
             */
            String type();
        }
    }
}
