require 'rspec'
require './lib/bullet'

describe Morphoid::Bullet do
  let (:bullet) { Morphoid::Bullet.new(1,2,:right, 2)}

  it "is alive initially" do
    expect(bullet.alive?).to be_truthy
    expect(bullet.x).to eq(1)
    expect(bullet.y).to eq(2)
    expect(bullet.direction).to eq(:right)
  end

  it "appears differently" do
    window = double()
    expect(window).to receive(:mvaddstr).with(2,1,'-')
    bullet.render(window)

    bullet.kill

    expect(window).to receive(:mvaddstr).with(2,1,'*')
    bullet.render(window)
  end

  it "decoys and explodes" do
    bullet.do_step
    expect(bullet.alive?).to be_truthy
    expect(bullet.x).to eq 2
    expect(bullet.y).to eq 2

    bullet.do_step
    expect(bullet.alive?).to be_falsey
    expect(bullet.x).to eq 3
    expect(bullet.y).to eq 2
  end

end


