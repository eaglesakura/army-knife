
platform :project do
  lane :doctor do
    gradle(task: "dependencyUpdates")
    $projects.each do |project|
        copy_artifacts(
          target_path: "artifacts/#{project}",
          artifacts: ["#{project}/build/dependencyUpdates"],
        )
    end
  end

  lane :docker_pull do
    sh <<-EOS
        gcloud docker -- pull "#{$docker_image}"
    EOS
  end

  lane :docker_push do
    sh <<-EOS
        gcloud docker -- push "#{$docker_image}"
    EOS
  end

  lane :docker_build do
    sh <<-EOS
        cd ../dockerfiles

        docker build \
          -t "#{$docker_image}" \
          -f build.dockerfile .
    EOS
  end
end
